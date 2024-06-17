package com.example.MMP.challenge.attendance;

import com.example.MMP.challenge.challengeActivity.ChallengeActivity;
import com.example.MMP.challengeGroup.ChallengeGroup;
import com.example.MMP.siteuser.SiteUser;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "user_id", nullable = false)
    private SiteUser siteUser;

    private LocalDate date;

    private boolean present;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private long totalTime; // 초 단위로 저장

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = true)
    private ChallengeGroup challengeGroup;

    @OneToOne(mappedBy = "attendance", fetch = FetchType.LAZY)
    @JsonBackReference
    private ChallengeActivity challengeActivity;

    @PrePersist
    @PreUpdate
    private void calculateTotalTime() {
        if (startTime != null && endTime != null) {
            this.totalTime = java.time.Duration.between(startTime, endTime).getSeconds();
        } else {
            this.totalTime = 0;
        }
    }
}
