package com.example.MMP.challenge.challenge;

import com.example.MMP.challenge.attendance.Attendance;
import com.example.MMP.challenge.attendance.AttendanceRepository;
import com.example.MMP.challenge.attendance.AttendanceService;
import com.example.MMP.challenge.challengeActivity.ChallengeActivity;
import com.example.MMP.challenge.challengeActivity.ChallengeActivityRepository;
import com.example.MMP.challenge.challengeUser.ChallengeUser;
import com.example.MMP.challenge.challengeUser.ChallengeUserRepository;
import com.example.MMP.challenge.challengeUser.ChallengeUserService;
import com.example.MMP.challenge.userWeight.UserWeightService;
import com.example.MMP.chat.ChatRoom;
import com.example.MMP.chat.ChatRoomRepository;
import com.example.MMP.siteuser.SiteUser;
import com.example.MMP.siteuser.SiteUserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final ChallengeUserRepository challengeUserRepository;
    private final SiteUserRepository siteUserRepository;
    private final UserWeightService userWeightService;
    private  final ChallengeActivityRepository challengeActivityRepository;
    private final ChallengeUserService challengeUserService;
    private final AttendanceService attendanceService;
    private final AttendanceRepository attendanceRepository;

    public Challenge create(String name, String description, LocalDateTime startDate, LocalDateTime endDate, int requiredPoint, String type, Double targetWeightLoss, Integer targetExerciseMinutes) {
        Challenge challenge = new Challenge();
        challenge.setName(name);
        challenge.setDescription(description);
        challenge.setOpenDate(startDate);
        challenge.setCloseDate(endDate);
        challenge.setRequiredPoint(requiredPoint);
        challenge.setType(type);
        challenge.setExpiration (false);

        if ("weight".equals(type) && targetWeightLoss != null) {
            challenge.setTargetWeightLoss(targetWeightLoss);
        }

        if ("exerciseTime".equals(type) && targetExerciseMinutes != null) {
            Integer targetExerciseChangeMinutes = targetExerciseMinutes*60;
            challenge.setTargetExerciseSeconds (targetExerciseChangeMinutes);
        }

        return challengeRepository.save(challenge);
    }
    public Challenge getChallenge(Long id) {
        Optional<Challenge> challengeOptional = challengeRepository.findById(id);
        return challengeOptional.orElseThrow(() -> new EntityNotFoundException ("Challenge not found with id: " + id));
    }

    public void delete(Challenge challenge){
       challengeRepository.delete (challenge);
    }

    public Challenge expiration(Long challengeId){
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new RuntimeException("챌린지를 찾을 수 없습니다."));
        challenge.setExpiration (true);
        return challengeRepository.save (challenge);
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
            System.out.println("참여 성공: " + challengeUser.getId());

            // ChallengeActivity 생성
            ChallengeActivity challengeActivity = new ChallengeActivity();
            challengeActivity.setActiveDate(LocalDateTime.now());
            challengeActivity.setChallenge(challenge);
            challengeActivityRepository.save(challengeActivity);
            System.out.println("ChallengeActivity 저장 성공: " + challengeActivity.getId());
        } else {
            challengeUserRepository.delete(optionalChallengeUser.get());
            System.out.println("참여 취소: " + optionalChallengeUser.get().getId());
        }
    }

    public void participateInChallengeWithWeight(Long challengeId, Principal principal, double weight) {
        String userId = principal.getName();
        SiteUser siteUser = siteUserRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(() -> new RuntimeException("챌린지를 찾을 수 없습니다."));

        Optional<ChallengeUser> optionalChallengeUser = challengeUserRepository.findByChallengeAndSiteUser(challenge, siteUser);

        ChallengeUser challengeUser;
        if (optionalChallengeUser.isEmpty()) {
            // ChallengeUser 생성
            challengeUser = new ChallengeUser();
            challengeUser.setChallenge(challenge);
            challengeUser.setSiteUser(siteUser);
            challengeUser.setStartDate(LocalDateTime.now());
            challengeUser.setEndDate(challenge.getCloseDate());
            challengeUser.setAchievementRate(0);
            challengeUser.setSuccess(false);
            challengeUser.setInitialWeight(weight); // 초기 몸무게 설정
            challengeUserRepository.save(challengeUser);
            System.out.println("참여 성공: " + challengeUser.getId());
        } else {
            // 이미 참여한 경우, 해당 ChallengeUser 가져오기
            challengeUser = optionalChallengeUser.get();
            System.out.println("이미 참여 중: " + challengeUser.getId());
        }

        // 초기 또는 최신 몸무게 기록
        userWeightService.recordWeight(siteUser.getId(), weight);
    }

    @Transactional
    public void updateWeight(Long challengeId, Principal principal, double weight) {
        String userId = principal.getName();
        SiteUser siteUser = siteUserRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(() -> new RuntimeException("챌린지를 찾을 수 없습니다."));

        // 변경된 몸무게 기록
        userWeightService.recordWeight(siteUser.getId(), weight);

        // 업데이트된 몸무게를 기준으로 달성률 갱신
        challengeUserRepository.findByChallengeAndSiteUser(challenge, siteUser).ifPresent(challengeUser -> {
            double initialWeight = challengeUser.getInitialWeight();  // ChallengeUser 객체에서 초기 몸무게 가져오기
            double targetWeightLoss = challenge.getTargetWeightLoss();
            double currentWeightLoss = initialWeight - weight;
            double achievementRate = (currentWeightLoss / targetWeightLoss) * 100;
            challengeUser.setAchievementRate(achievementRate);
            challengeUserRepository.save(challengeUser);

            // 달성률이 100% 이상인 경우 챌린지 성공 처리
            if (achievementRate >= 100) {
                challengeUserService.markChallengeAsSuccessful(challengeUser.getId());
            }
        });
    }

    @Transactional(readOnly = true)
    public List<Challenge> getParticipatedChallenges(Long userId) {
        SiteUser siteUser = siteUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        List<ChallengeUser> challengeUsers = challengeUserRepository.findBySiteUser(siteUser);
        return challengeUsers.stream()
                .map(ChallengeUser::getChallenge)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Map<String, List<Challenge>> getChallengesByStatus(Long userId) {
        SiteUser siteUser = siteUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        List<ChallengeUser> challengeUsers = challengeUserRepository.findBySiteUser(siteUser);

        List<Challenge> ongoingChallenges = new ArrayList<>();
        List<Challenge> successfulChallenges = new ArrayList<>();
        List<Challenge> failedChallenges = new ArrayList<>();

        for (ChallengeUser challengeUser : challengeUsers) {
            if (challengeUser.isSuccess()) {
                successfulChallenges.add(challengeUser.getChallenge());
            } else if (challengeUser.getEndDate().isBefore(LocalDateTime.now())) {
                failedChallenges.add(challengeUser.getChallenge());
            } else {
                ongoingChallenges.add(challengeUser.getChallenge());
            }
        }

        Map<String, List<Challenge>> challengesByStatus = new HashMap<>();
        challengesByStatus.put("ongoing", ongoingChallenges);
        challengesByStatus.put("successful", successfulChallenges);
        challengesByStatus.put("failed", failedChallenges);
        return challengesByStatus;
    }

    @Transactional
    public void updateAchievementRate(Long challengeUserId) {
        ChallengeUser challengeUser = challengeUserRepository.findById(challengeUserId)
                .orElseThrow(() -> new RuntimeException("챌린지 유저를 찾을 수 없습니다"));

        Challenge challenge = challengeUser.getChallenge();
        Long siteUserId = challengeUser.getSiteUser().getId();
        double achievementRate = 0;

        if ("weight".equals(challenge.getType())) {
            // 기존 몸무게 기반 달성률 계산 로직
        } else if ("exerciseTime".equals(challenge.getType())) {
            long totalExerciseTime = attendanceService.calculateTotalExerciseTimeBetweenDates(
                    siteUserId,
                    challenge.getOpenDate().toLocalDate(),
                    challenge.getCloseDate().toLocalDate()
            );
            int targetExerciseMinutes = challenge.getTargetExerciseSeconds ();
            achievementRate = ((double) totalExerciseTime / targetExerciseMinutes) * 100;
        }

        challengeUser.setAchievementRate(achievementRate);
        challengeUserRepository.save(challengeUser);

        if (achievementRate >= 100) {
            challengeUserService.markChallengeAsSuccessful(challengeUserId);
        }
    }

    public void participateInChallengeWithExerciseTime(Long challengeId, Principal principal, int exerciseTime) {
        String userId = principal.getName();
        SiteUser siteUser = siteUserRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(() -> new RuntimeException("챌린지를 찾을 수 없습니다."));

        Optional<ChallengeUser> optionalChallengeUser = challengeUserRepository.findByChallengeAndSiteUser(challenge, siteUser);

        ChallengeUser challengeUser;
        if (optionalChallengeUser.isEmpty()) {
            // ChallengeUser 생성
            challengeUser = new ChallengeUser();
            challengeUser.setChallenge(challenge);
            challengeUser.setSiteUser(siteUser);
            challengeUser.setStartDate(LocalDateTime.now());
            challengeUser.setEndDate(challenge.getCloseDate());
            challengeUser.setAchievementRate(0);
            challengeUser.setSuccess(false);
            challengeUser.setInitialExerciseTime(0);
            challengeUserRepository.save(challengeUser);
            System.out.println("참여 성공: " + challengeUser.getId());
        } else {
            // 이미 참여한 경우, 해당 ChallengeUser 가져오기
            challengeUser = optionalChallengeUser.get();
            System.out.println("이미 참여 중: " + challengeUser.getId());
        }

        // ChallengeActivity 생성
        ChallengeActivity challengeActivity = new ChallengeActivity();
        challengeActivity.setActiveDate(LocalDateTime.now());
        challengeActivity.setChallenge(challenge);
        challengeActivityRepository.save(challengeActivity);

        // 운동 시간에 따른 달성률 업데이트
        updateAchievementRate(challengeUser.getId());
    }

    @Transactional
    public void updateExerciseTime(Long challengeId, Principal principal) {
        String userId = principal.getName();
        SiteUser siteUser = siteUserRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new RuntimeException("챌린지를 찾을 수 없습니다."));
        Long siteUserId = siteUser.getId();

        Long challengeUserId = challengeUserRepository.findByChallengeAndSiteUser(challenge, siteUser)
                .orElseThrow(() -> new RuntimeException("챌린지 유저를 찾을 수 없습니다.")).getId();

        LocalDateTime challengeStartDate = challengeUserRepository.findById (challengeUserId).get ().getStartDate ();

        // AttendanceService를 사용하여 운동 시간 계산
        List<Attendance> attendanceList = attendanceRepository.findBySiteUserId (siteUserId);

        long totalExerciseTimeInSeconds = 0L;
        for (Attendance attendance : attendanceList) {
            if (attendance.getStartTime ().isAfter(challengeStartDate)) {
                totalExerciseTimeInSeconds += attendance.getTotalTime();
            }
        }

        final long finalTotalExerciseTimeInSeconds = totalExerciseTimeInSeconds;

        challengeUserRepository.findByChallengeAndSiteUser(challenge, siteUser).ifPresent(challengeUser -> {
            long initialExerciseTime = challengeUser.getInitialExerciseTime(); // 초 단위
            long targetExerciseTime = challenge.getTargetExerciseSeconds(); // 초 단위
            long currentExerciseTime = initialExerciseTime + finalTotalExerciseTimeInSeconds;

            int achievementRate = (int) ((currentExerciseTime * 100) / targetExerciseTime);
            challengeUser.setAchievementRate(achievementRate);
            challengeUserRepository.save(challengeUser);

            // 달성률이 100% 이상인 경우 챌린지 성공 처리
            if (achievementRate >= 100) {
                challengeUserService.markChallengeAsSuccessful(challengeUser.getId());
            }
        });
    }

}



