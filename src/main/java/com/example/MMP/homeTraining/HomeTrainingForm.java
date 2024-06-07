package com.example.MMP.homeTraining;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HomeTrainingForm {
    @Size(max = 200)
    private String content;

    private String videoUrl;
}
