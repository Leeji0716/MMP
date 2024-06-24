package com.example.MMP.trainer;

import com.example.MMP.siteuser.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Trainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imagePath;

    private String introduce;

    private String classType;

    private String specialization;

    private String keyword;

    @OneToOne
    private SiteUser userTrainer;
}
