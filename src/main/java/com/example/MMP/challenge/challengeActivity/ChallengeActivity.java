package com.example.MMP.challenge.challengeActivity;

import com.example.MMP.attendance.Attendance;
import com.example.MMP.challenge.challenge.Challenge;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class ChallengeActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime activeDate;

    private int duration;

    private int weight;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attendance_id", nullable = true)
    private Attendance attendance;

    private int exerciseTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    // 기본 생성자에서 새 Attendance 객체 생성 부분 제거
    public ChallengeActivity() {
        // this.attendance = new Attendance();
    }

}
