package com.example.MMP.challenge.challengeActivity;

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

    public void addActivity(Long challengeId, LocalDateTime activeDate, int duration, int weight, int exerciseTime) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new RuntimeException("챌린지를 찾을 수 없습니다."));

        ChallengeActivity activity = new ChallengeActivity();
        activity.setChallenge(challenge);
        activity.setActiveDate(activeDate);
        activity.setDuration(duration);
        activity.setWeight(weight);
        activity.setExerciseTime(exerciseTime);
        activity.setAttendance(null); // 명시적으로 null로 설정

        challengeActivityRepository.save(activity);
    }
}
