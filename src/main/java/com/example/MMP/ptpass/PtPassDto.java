package com.example.MMP.ptpass;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PtPassDto {
    @NotEmpty(message = "회원권 이름을 입력해주세요.")
    private String passName;

    @NotEmpty(message = "회원권 설명을 입력해주세요.")
    private String passTitle;


    private int passCount;


    private int passPrice;

}
