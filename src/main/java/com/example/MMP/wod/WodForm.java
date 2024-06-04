package com.example.MMP.wod;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WodForm {
    @Size(max = 200)
    private String content;

    private String imagePath;
}
