package com.example.MMP.usercoupon;

import com.example.MMP.coupon.Coupon;
import com.example.MMP.siteuser.SiteUser;
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

    public void create(Coupon coupon, SiteUser siteUser){
        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setName(coupon.getName());
        userCoupon.setPoint(coupon.getPoint());
        userCoupon.setDiscount(coupon.getDiscount());
        userCoupon.setSiteUser(siteUser);
        userCouponRepository.save(userCoupon);
    }

}
