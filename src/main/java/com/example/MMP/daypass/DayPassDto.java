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


    private int dayPassPrice;



    private int dayPassDays;
}
