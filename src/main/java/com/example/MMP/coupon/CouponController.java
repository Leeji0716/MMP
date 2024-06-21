package com.example.MMP.coupon;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/coupon")
public class CouponController {
    private final CouponService couponService;

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
    public String list(Model model){
        List<Coupon> couponList = couponService.getAll();
        model.addAttribute("couponList", couponList);
        return "coupon/coupon_list";
    }

}
