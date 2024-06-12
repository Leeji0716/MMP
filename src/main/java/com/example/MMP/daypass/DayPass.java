package com.example.MMP.daypass;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class DayPass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long passId;

    @Column(unique = true)
    private String passName;

    private String passTitle;

    private int passPrice;

    private int passDays;

}
