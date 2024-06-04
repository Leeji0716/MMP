package com.example.MMP.siteuser;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AdminDto {
    @NotEmpty(message = "이름은 필수입니다.")
    private String name;

    @NotEmpty(message = "성별은 필수입니다.")
    private String gender;

    @NotEmpty(message = "핸드폰번호는 필수입니다.")
    private String number;

    @NotEmpty(message = "생일은 필수입니다.")
    private String birthDate;

    @NotEmpty(message = "이메일은 필수입니다.")
    @Email(message = "이메일형식이 아닙니다.")
    private String email;
}
