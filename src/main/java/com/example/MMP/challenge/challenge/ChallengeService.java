package com.example.MMP.challenge.challenge;

import com.example.MMP.challenge.attendance.AttendanceRepository;
import com.example.MMP.challenge.attendance.AttendanceService;
import com.example.MMP.challenge.challengeActivity.ChallengeActivity;
import com.example.MMP.challenge.challengeActivity.ChallengeActivityRepository;
import com.example.MMP.challenge.challengeUser.ChallengeUser;
import com.example.MMP.challenge.challengeUser.ChallengeUserRepository;
import com.example.MMP.challenge.userWeight.UserWeightService;
import com.example.MMP.siteuser.SiteUser;
import com.example.MMP.siteuser.SiteUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final ChallengeUserRepository challengeUserRepository;
    private final SiteUserRepository siteUserRepository;
    private final UserWeightService userWeightService;
    private  final ChallengeActivityRepository challengeActivityRepository;
    private final AttendanceService attendanceService;

    public Challenge create(String name, String description, LocalDateTime startDate, LocalDateTime endDate, int requiredPoint, String type, Double targetWeightLoss, Integer targetExerciseMinutes) {
        Challenge challenge = new Challenge();
        challenge.setName(name);
        challenge.setDescription(description);
        challenge.setOpenDate(startDate);
        challenge.setCloseDate(endDate);
        challenge.setRequiredPoint(requiredPoint);
        challenge.setType(type);

        if ("weight".equals(type) && targetWeightLoss != null) {
            challenge.setTargetWeightLoss(targetWeightLoss);
        }

        if ("exerciseTime".equals(type) && targetExerciseMinutes != null) {
            challenge.setTargetExerciseMinutes(targetExerciseMinutes);
        }

        return challengeRepository.save(challenge);
    }

    public void participateInChallenge(Long challengeId, Principal principal) {
        String userId = principal.getName();
        SiteUser siteUser = siteUserRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new RuntimeException("챌린지를 찾을 수 없습니다."));

        Optional<ChallengeUser> optionalChallengeUser = challengeUserRepository.findByChallengeAndSiteUser(challenge, siteUser);

        if (optionalChallengeUser.isEmpty()) {
            // ChallengeUser 생성
            ChallengeUser challengeUser = new ChallengeUser();
            challengeUser.setChallenge(challenge);
            challengeUser.setSiteUser(siteUser);
            challengeUser.setStartDate(LocalDateTime.now());
            challengeUser.setEndDate(challenge.getCloseDate());
            challengeUser.setSuccess(false);
            challengeUserRepository.save(challengeUser);
        } else {
            challengeUserRepository.delete(optionalChallengeUser.get());
        }
    }


    public void participateInChallengeWithWeight(Long challengeId, Principal principal, double weight) {
        String userId = principal.getName();
        SiteUser siteUser = siteUserRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(() -> new RuntimeException("챌린지를 찾을 수 없습니다."));

        Optional<ChallengeUser> optionalChallengeUser = challengeUserRepository.findByChallengeAndSiteUser(challenge, siteUser);

        if (optionalChallengeUser.isEmpty()) {
            // ChallengeUser 생성
            ChallengeUser challengeUser = new ChallengeUser();
            challengeUser.setChallenge(challenge);
            challengeUser.setSiteUser(siteUser);
            challengeUser.setStartDate(LocalDateTime.now());
            challengeUser.setEndDate(challenge.getCloseDate());
            challengeUser.setSuccess(false);
            challengeUserRepository.save(challengeUser);

            // 초기 몸무게 기록
            userWeightService.recordInitialWeight(siteUser.getId(), weight);
        } else {
            challengeUserRepository.delete(optionalChallengeUser.get());
        }
    }

    public void updateWeight(Long challengeId, Principal principal, double weight) {
        String userId = principal.getName();
        SiteUser siteUser = siteUserRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(() -> new RuntimeException("챌린지를 찾을 수 없습니다."));

        // 변경된 몸무게 기록
        userWeightService.recordWeight(siteUser.getId(), weight);

        // 업데이트된 몸무게를 기준으로 달성률 갱신
        challengeUserRepository.findByChallengeAndSiteUser(challenge, siteUser).ifPresent(challengeUser -> {
            double initialWeight = userWeightService.getInitialWeight(siteUser.getId());
            double targetWeightLoss = challenge.getTargetWeightLoss();
            double currentWeightLoss = initialWeight - weight;
            double achievementRate = (currentWeightLoss / targetWeightLoss) * 100;
            challengeUser.setAchievementRate(achievementRate);
            challengeUserRepository.save(challengeUser);
        });
    }
}



