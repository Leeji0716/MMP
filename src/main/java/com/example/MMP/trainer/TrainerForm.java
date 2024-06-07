package com.example.MMP.trainer;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrainerForm {
    @Size(max = 200)
    @NotEmpty(message = "123")
    private String introduce;

    @NotEmpty(message = "123")
    private String trainerName;
}
