package com.example.MMP.trainer;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrainerForm {

    @NotEmpty(message = "트레이너 이름은 필수 입니다.")
    private String trainerName;

    @NotEmpty(message = "트레이너 소개는 필수 입니다.")
    private String introduce;

    private String gender;

    private String classType;

    private String specialization;
}
