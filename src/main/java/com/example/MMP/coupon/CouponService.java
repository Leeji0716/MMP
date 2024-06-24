package com.example.MMP.coupon;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;

    public void create(String name, int point, int discount) {
        Coupon coupon = new Coupon();
        coupon.setName(name);
        coupon.setPoint(point);
        coupon.setDiscount(discount);

        couponRepository.save(coupon);
    }

    public List<Coupon> getAll() {
        return couponRepository.findAll();
    }

    public Coupon getCoupon(Long id) {
        Coupon coupon = couponRepository.findById(id).orElseThrow();
        return coupon;
    }

    public void delete(Coupon useCoupon) {
        couponRepository.delete(useCoupon);
    }

    public void save(Coupon useCoupon) {
        couponRepository.save(useCoupon);
    }

    public Coupon findById(Long id) {
        return couponRepository.findById(id).orElseThrow();
    }
}
