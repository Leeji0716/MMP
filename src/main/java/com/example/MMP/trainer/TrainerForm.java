package com.example.MMP.trainer;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrainerForm {
    private Long userTrainerId;

    @NotEmpty(message = "트레이너 소개는 필수 입니다.")
    private String introduce;

    @NotEmpty
    private String classType;

    @NotEmpty
    private String specialization;
}
