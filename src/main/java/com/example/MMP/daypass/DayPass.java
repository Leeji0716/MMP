package com.example.MMP.daypass;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class DayPass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dayPassId;

    private String dayPassName;

    private String dayPassTitle;

    private LocalDateTime dayPassStart;

    private LocalDateTime dayPassFinish;

    private int dayPassPrice;

    private int dayPassDays;

}
