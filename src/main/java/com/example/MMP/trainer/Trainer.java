package com.example.MMP.trainer;

import com.example.MMP.siteuser.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
public class Trainer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String trainerId;

    private String password;

    @Column(unique = true)
    private String email;

    private String gender;

    private String number;

    private String birthDay;


    private String userRole;

    @ManyToMany
    List<SiteUser> memberList = new ArrayList<>();
}
