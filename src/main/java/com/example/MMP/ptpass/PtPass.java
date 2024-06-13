package com.example.MMP.ptpass;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Getter
@Setter
public class PtPass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long passId;

    @Column(unique = true)
    private String passName;

    private String passTitle;

    private int passCount;

    private int passPrice;

    private int passDays;

}
