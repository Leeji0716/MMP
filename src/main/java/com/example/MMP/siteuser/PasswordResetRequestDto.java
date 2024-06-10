package com.example.MMP.siteuser;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetRequestDto {
    @NotEmpty(message = "아이디는 필수 항목입니다.")
    private String userId;

    @NotEmpty(message = "이메일은 필수 항목입니다.")
    @Email(message = "유효한 이메일 주소를 입력해주세요.")
    private String email;
}
