package com.example.MMP.attendance;


import com.example.MMP.challenge.challengeActivity.ChallengeActivity;
import com.example.MMP.siteuser.SiteUser;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonBackReference
    private SiteUser siteUser;

    private LocalDate date;

    private boolean present;

    @OneToOne(mappedBy = "attendance", fetch = FetchType.LAZY)
    private ChallengeActivity challengeActivity;

}
