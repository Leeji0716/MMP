package com.example.MMP.coupon;

import com.example.MMP.siteuser.SiteUser;
import com.example.MMP.siteuser.SiteUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/coupon")
public class CouponController {
    private final CouponService couponService;
    private final SiteUserService siteUserService;

    @GetMapping("/create")
    public String create(CouponForm couponForm){
        return "coupon/coupon_create";
    }

    @PostMapping("/create")
    public String create(@Valid CouponForm couponForm, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "coupon/coupon_create";
        }
        couponService.create(couponForm.getName(), Integer.parseInt(couponForm.getPoint()), Integer.parseInt(couponForm.getDiscount()));

        return "redirect:/coupon/list";
    }

    @GetMapping("/list")
    public String list(Model model, Principal principal){
        List<Coupon> couponList = couponService.getAll();
        SiteUser siteUser = siteUserService.findByUserName(principal.getName());
        String points = String.valueOf(siteUser.getPoint().getPoints());
        List<Coupon> userCouponList = siteUser.getCouponList();

        model.addAttribute("couponList", couponList);
        model.addAttribute("userCouponList", userCouponList);
        model.addAttribute("points", points);
        System.out.println(userCouponList.size());
        return "coupon/coupon_list";
    }

    @GetMapping("/purchase/{id}")
    public String purchase(@PathVariable("id") Long id, Model model, Principal principal){
        Coupon coupon = couponService.getCoupon(id);
        List<Coupon> couponList = couponService.getAll();
        SiteUser siteUser = siteUserService.findByUserName(principal.getName());
        int a = siteUser.getPoint().getPoints() - coupon.getPoint();

        siteUser.getPoint().setPoints(a);
        String points = String.valueOf(siteUser.getPoint().getPoints());

        siteUser.getCouponList().add(coupon);
        siteUserService.save(siteUser);

        model.addAttribute("couponList", couponList);
        model.addAttribute("points", points);
        return "redirect:/";
    }
}
