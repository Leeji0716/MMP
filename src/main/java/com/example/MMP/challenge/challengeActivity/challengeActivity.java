package com.example.MMP.challenge.challengeActivity;

import com.example.MMP.challenge.challenge.Challenge;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class challengeActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime activeDate;

    private int duration;

    private int weight;

    private int attendance;

    private int exerciseTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

}
