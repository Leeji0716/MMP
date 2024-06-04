package com.example.MMP.wod;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WodForm {
    @Size(max = 200)
    private String content;

    @NotEmpty(message = "이미지 경로는 필수항목입니다.")
    private String imagePath;

}
