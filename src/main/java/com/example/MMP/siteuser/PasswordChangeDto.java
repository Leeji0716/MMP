package com.example.MMP.siteuser;


import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordChangeDto {
    @NotEmpty(message = "현재 비밀번호는 필수 항목입니다.")
    private String currentPassword;

    @NotEmpty(message = "새 비밀번호는 필수 항목입니다.")
    private String newPassword;
}
