package com.example.MMP.challenge.challengeUser;

import com.example.MMP.challenge.attendance.AttendanceService;
import com.example.MMP.challenge.challenge.Challenge;
import com.example.MMP.challenge.challengeActivity.ChallengeActivity;
import com.example.MMP.challenge.challengeActivity.ChallengeActivityRepository;
import com.example.MMP.challenge.userWeight.UserWeight;
import com.example.MMP.challenge.userWeight.UserWeightService;
import com.example.MMP.point.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ChallengeUserService {

    private final ChallengeUserRepository challengeUserRepository;
    private final UserWeightService userWeightService;
    private final PointService pointService;


    @Transactional
    public void markChallengeAsSuccessful(Long challengeUserId) {
        ChallengeUser challengeUser = challengeUserRepository.findById(challengeUserId)
                .orElseThrow(() -> new RuntimeException("챌린지 유저를 찾을 수 없습니다"));
        challengeUser.setSuccess(true);
        challengeUserRepository.save(challengeUser);

        // 챌린지 성공 시 requiredPoint를 추가
        Challenge challenge = challengeUser.getChallenge();
        int pointsToAdd = challenge.getRequiredPoint();
        pointService.addPoints(challengeUser.getSiteUser().getId(), pointsToAdd);
    }

    // 챌린지 성공 여부와 달성률 업데이트
    @Transactional
    public void updateAchievementRate(Long challengeUserId) {
        ChallengeUser challengeUser = challengeUserRepository.findById(challengeUserId)
                .orElseThrow(() -> new RuntimeException("챌린지 유저를 찾을 수 없습니다"));

        Challenge challenge = challengeUser.getChallenge();
        Long siteUserId = challengeUser.getSiteUser().getId();
        double achievementRate = 0;

        if ("weight".equals(challenge.getType())) {
            List<UserWeight> weights = userWeightService.getUserWeights(siteUserId);
            if (!weights.isEmpty()) {
                double initialWeight = weights.get(0).getWeight();
                double latestWeight = weights.get(weights.size() - 1).getWeight();
                double weightLoss = initialWeight - latestWeight;
                double targetWeightLoss = challenge.getTargetWeightLoss();
                achievementRate = (weightLoss / targetWeightLoss) * 100;
            }
        }

        challengeUser.setAchievementRate(achievementRate);
        challengeUserRepository.save(challengeUser);

        // 달성률이 100%인 경우 챌린지 성공 처리
        if (achievementRate >= 100) {
            markChallengeAsSuccessful(challengeUserId);
        }
    }

    private double calculateActivityRate(List<ChallengeActivity> activities, Integer targetExerciseMinutes) {
        int totalActivities = activities.size();
        if (totalActivities == 0) {
            return 0;
        }

        int successfulActivities = (int) activities.stream()
                .filter(a -> a.getExerciseTime() >= targetExerciseMinutes)
                .count();
        return ((double) successfulActivities / totalActivities) * 100;
    }
}
