package com.example.MMP.challenge.challenge;

import com.example.MMP.challenge.challengeActivity.ChallengeActivity;
import com.example.MMP.challenge.challengeUser.ChallengeUser;
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

    // 몸무게 챌린지
    private Double targetWeightLoss;

    // 운동시간 챌린지
    private Integer targetExerciseSeconds;

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChallengeActivity> challengeActivities = new ArrayList<>();

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChallengeUser> challengeUsers = new ArrayList<>();

    private boolean expiration;
}
