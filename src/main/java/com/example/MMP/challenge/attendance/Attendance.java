package com.example.MMP.challenge.attendance;

import com.example.MMP.challenge.challengeActivity.ChallengeActivity;
import com.example.MMP.siteuser.SiteUser;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @OneToOne(mappedBy = "attendance", fetch = FetchType.LAZY)
    @JsonBackReference
    private ChallengeActivity challengeActivity;
}
