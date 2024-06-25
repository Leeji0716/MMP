package com.example.MMP.siteuser;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindIdDto {

    @NotEmpty(message = "이름은 필수 항목입니다.")
    private String name;

    @NotEmpty(message = "생년월일은 필수 항목입니다.")
    private String birthDate;

    @NotEmpty(message = "이메일은 필수 항목입니다.")
    private String email;
}
