package com.example.MMP.siteuser;

import com.example.MMP.daypass.DayPass;
import com.example.MMP.ptpass.PtPass;
import com.example.MMP.trainer.Trainer;
import com.example.MMP.wod.Wod;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class SiteUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String userId;

    private String password;

    private String name;

    private String gender;

    private String number;

    private String birthDate;

    private String email;

    private String userRole;


    @ManyToMany
    List<Trainer> trainerList = new ArrayList<>();

    @OneToMany
    List<PtPass> ptPassList = new ArrayList<>();

    @OneToMany
    List<DayPass> dayPassList = new ArrayList<>();

    @OneToMany
    private List<Wod> wodList;

}
