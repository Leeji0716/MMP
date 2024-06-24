package com.example.MMP.usercoupon;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCouponService {
    private final UserCouponRepository userCouponRepository;

    public void save(UserCoupon userCoupon) {
    }

    public UserCoupon findById(Long coupon) {
        return userCouponRepository.findById(coupon).orElseThrow();
    }

    public void delete(UserCoupon userCoupon) {
        userCouponRepository.delete(userCoupon);
    }
}
