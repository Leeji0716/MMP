package com.example.MMP.siteuser;

import com.example.MMP.challenge.challenge.Challenge;
import com.example.MMP.userPass.UserDayPass;
import com.example.MMP.userPass.UserPtPass;
import jakarta.persistence.*;
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

    @OneToMany(mappedBy = "siteUser",cascade = CascadeType.REMOVE)
    List<UserPtPass> userPtPassList = new ArrayList<>();

    @OneToMany(mappedBy = "siteUser",cascade = CascadeType.REMOVE)
    List<UserDayPass> userDayPassList = new ArrayList<>();
//
//    @OneToMany
//    private List<Wod> wodList;

    @OneToMany
    private List<Challenge> challenges = new ArrayList<> ();

}
