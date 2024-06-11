package com.example.MMP.challenge.challengeActivity;

import com.example.MMP.challenge.attendance.Attendance;
import com.example.MMP.challenge.attendance.AttendanceRepository;
import com.example.MMP.challenge.challenge.Challenge;
import com.example.MMP.challenge.challenge.ChallengeRepository;
import com.example.MMP.challenge.challengeUser.ChallengeUser;
import com.example.MMP.challenge.challengeUser.ChallengeUserRepository;
import com.example.MMP.challenge.challengeUser.ChallengeUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ChallengeActivityService {

    private final ChallengeActivityRepository challengeActivityRepository;
    private final ChallengeRepository challengeRepository;
    private final AttendanceRepository attendanceRepository;
    private final ChallengeUserRepository challengeUserRepository;
    private final ChallengeUserService challengeUserService;


    public void participate(Long challengeId, Long attendanceId, int duration, int exerciseTime, int weight) {
        ChallengeActivity challengeActivity = new ChallengeActivity ();
        challengeActivity.setActiveDate (LocalDateTime.now ());
        challengeActivity.setDuration (duration);
        challengeActivity.setWeight (weight);
        challengeActivity.setExerciseTime (exerciseTime);

        Challenge challenge = challengeRepository.findById (challengeId).orElse (null);
        if (challenge == null) {
            return;
        }
        challengeActivity.setChallenge (challenge);

        if (attendanceId != null) {
            Attendance attendance = attendanceRepository.findById (attendanceId).orElse (null);
            if (attendance == null) {
                return;
            }
            challengeActivity.setAttendance (attendance);
        } else {
            challengeActivity.setAttendance (null);
        }

        challengeActivityRepository.save (challengeActivity);

        // 활동 기록 후 달성률 업데이트
        List<ChallengeUser> challengeUsers = challengeUserRepository.findByChallenge (challenge);
        for (ChallengeUser challengeUser : challengeUsers) {
            challengeUserService.updateAchievementRate (challengeUser.getId ());
        }
    }
}
