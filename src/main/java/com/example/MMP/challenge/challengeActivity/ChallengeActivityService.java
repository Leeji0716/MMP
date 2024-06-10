package com.example.MMP.challenge.challengeActivity;

import com.example.MMP.attendance.Attendance;
import com.example.MMP.attendance.AttendanceRepository;
import com.example.MMP.challenge.challenge.Challenge;
import com.example.MMP.challenge.challenge.ChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ChallengeActivityService {

    private final ChallengeActivityRepository challengeActivityRepository;
    private final ChallengeRepository challengeRepository;
    private final AttendanceRepository attendanceRepository;


    public void participate(Long challengeId, Long attendanceId, int duration, int exerciseTime, int weight) {
        ChallengeActivity challengeActivity = new ChallengeActivity();
        challengeActivity.setActiveDate(LocalDateTime.now());
        challengeActivity.setDuration(duration);
        challengeActivity.setWeight(weight);
        challengeActivity.setExerciseTime(exerciseTime);

        Challenge challenge = challengeRepository.findById(challengeId).orElse(null);
        if (challenge == null) {
            return;
        }
        challengeActivity.setChallenge(challenge);

        if (attendanceId != null) {
            Attendance attendance = attendanceRepository.findById(attendanceId).orElse(null);
            if (attendance == null) {
                return;
            }
            challengeActivity.setAttendance(attendance);
        } else {
            challengeActivity.setAttendance(null); // 기본값 설정
        }

        challengeActivityRepository.save(challengeActivity);
    }
}
