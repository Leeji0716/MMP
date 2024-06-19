package com.example.MMP.siteuser;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CalorieForm {

    @NotEmpty(message = "성별은 필수입니다.")
    private String gender;

    @NotEmpty(message = "키는 필수입니다.")
    private String height;

    @NotEmpty(message = "몸무게는 필수입니다.")
    private String weight;
}
