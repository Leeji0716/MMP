package com.example.MMP.coupon;

import com.example.MMP.siteuser.SiteUser;
import com.example.MMP.siteuser.SiteUserService;
import com.example.MMP.usercoupon.UserCoupon;
import com.example.MMP.usercoupon.UserCouponRepository;
import com.example.MMP.usercoupon.UserCouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/coupon")
public class CouponController {
    private final CouponService couponService;
    private final SiteUserService siteUserService;
    private final UserCouponService userCouponService;
    private final UserCouponRepository userCouponRepository;

    @GetMapping("/create")
    public String create(CouponForm couponForm) {
        return "coupon/coupon_create";
    }

    @PostMapping("/create")
    public String create(@Valid CouponForm couponForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "coupon/coupon_create";
        }
        couponService.create(couponForm.getName(), Integer.parseInt(couponForm.getPoint()), Integer.parseInt(couponForm.getDiscount()));

        return "redirect:/coupon/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        couponService.delete(couponService.findById(id));
        return "redirect:/coupon/list";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") Long id, CouponForm couponForm, Model model) {
        Coupon coupon = couponService.findById(id);
        model.addAttribute("coupon", coupon);
        couponForm.setName(coupon.getName());
        couponForm.setPoint(String.valueOf(coupon.getPoint()));
        couponForm.setDiscount(String.valueOf(coupon.getDiscount()));
        return "coupon/coupon_create";
    }

    @PostMapping("/update/{id}")
    public String updateCoupon(@Valid CouponForm couponForm,
                               BindingResult bindingResult,
                               @PathVariable("id") Long id,
                               Model model) {

        if (bindingResult.hasErrors()) {
            return "coupon/coupon_create";
        }

        Coupon coupon = couponService.findById(id);
        coupon.setName(couponForm.getName());
        coupon.setPoint(Integer.parseInt(couponForm.getPoint()));
        coupon.setDiscount(Integer.parseInt(couponForm.getDiscount()));
        couponService.save(coupon);

        return "redirect:/coupon/list";
    }

    @GetMapping("/list")
    public String list(Model model, Principal principal) {
        List<Coupon> couponList = couponService.getAll();
        SiteUser siteUser = siteUserService.findByUserName(principal.getName());
        String points = String.valueOf(siteUser.getPoint().getPoints());
        List<UserCoupon> userCouponList = siteUser.getUserCouponList();

        model.addAttribute("couponList", couponList);
        model.addAttribute("userCouponList", userCouponList);
        model.addAttribute("points", points);
        System.out.println(userCouponList.size());
        return "coupon/coupon_list";
    }

    @GetMapping("/purchase/{id}")
    public String purchase(@PathVariable("id") Long id, Model model, Principal principal) {
        Coupon coupon = couponService.getCoupon(id);
        List<Coupon> couponList = couponService.getAll();
        SiteUser siteUser = siteUserService.findByUserName(principal.getName());
        int a = siteUser.getPoint().getPoints() - coupon.getPoint();

        siteUser.getPoint().setPoints(a);
        String points = String.valueOf(siteUser.getPoint().getPoints());

        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setName(coupon.getName());
        userCoupon.setPoint(coupon.getPoint());
        userCoupon.setDiscount(coupon.getDiscount());
        userCouponService.save(userCoupon);

        siteUser.getUserCouponList().add(userCoupon);
        siteUserService.save(siteUser);

        model.addAttribute("couponList", couponList);
        model.addAttribute("points", points);
        return "redirect:/coupon/list";
    }
}
