package com.example.MMP.siteuser;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDto {
    @NotEmpty(message = "이름은 필수입니다.")
    private String name;

    @NotEmpty(message = "성별은 필수입니다.")
    private String gender;

    @NotEmpty(message = "핸드폰번호는 필수입니다.")
    @Size(max = 11, min = 11, message = "11자리를 입력해주세요.")
    private String number;

    @NotEmpty(message = "생일은 필수입니다.")
    @Size(min = 6, max = 6, message = "생년월일 6자리를 입력해주세요.")
    private String birthDate;

    @NotEmpty(message = "이메일은 필수입니다.")
    @Email(message = "이메일형식이 아닙니다.")
    private String email;

    @NotEmpty(message = "권한은 필수입니다.")
    private String userRole;

    private int salary;

    private Long referrerId;
}
