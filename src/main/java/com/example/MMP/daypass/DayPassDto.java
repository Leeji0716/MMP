package com.example.MMP.daypass;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DayPassDto {

    @NotEmpty(message = "회원권 이름을 입력해주세요.")
    private String dayPassName;

    @NotEmpty(message = "회원권 설명을 입력해주세요.")
    private String dayPassTitle;

    @NotEmpty(message = "회원권 가격을 입력해주세요.")
    private int dayPassPrice;

    @NotEmpty(message = "회원권 일수를 입력해주세요.")
    private int dayPassDays;
}
