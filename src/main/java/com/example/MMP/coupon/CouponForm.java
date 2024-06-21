package com.example.MMP.coupon;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CouponForm {
    @Size(max = 20)
    private String name;

    @NotEmpty
    private String point;

    @NotEmpty
    private String discount;
}
