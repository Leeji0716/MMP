package com.example.MMP.challenge.challenge;

import com.example.MMP.challenge.challengeActivity.challengeActivity;
import com.example.MMP.challenge.challengeUser.challengeUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private LocalDateTime openDate;

    private LocalDateTime closeDate;

    private int requiredPoint;

    private String type;

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<challengeActivity> challengeActivities = new ArrayList<> ();

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<challengeUser> challengeUsers = new ArrayList<> ();


}
