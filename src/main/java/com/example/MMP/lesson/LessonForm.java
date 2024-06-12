package com.example.MMP.lesson;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
public class LessonForm {

    @NotNull
    @Size(max = 10)
    private String lessonName;

    @NotEmpty
    private String headCount;

    @NotNull
    private LocalDate lessonDate;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;
}
