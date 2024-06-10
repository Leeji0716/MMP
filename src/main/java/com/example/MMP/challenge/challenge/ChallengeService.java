package com.example.MMP.challenge.challenge;

import com.example.MMP.attendance.Attendance;
import com.example.MMP.attendance.AttendanceRepository;
import com.example.MMP.attendance.AttendanceService;
import com.example.MMP.challenge.challengeActivity.ChallengeActivity;
import com.example.MMP.challenge.challengeActivity.ChallengeActivityRepository;
import com.example.MMP.challenge.challengeUser.ChallengeUser;
import com.example.MMP.challenge.challengeUser.ChallengeUserRepository;
import com.example.MMP.siteuser.SiteUser;
import com.example.MMP.siteuser.SiteUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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
    private final AttendanceRepository attendanceRepository;
    private  final ChallengeActivityRepository challengeActivityRepository;
    private final AttendanceService attendanceService;

    public void updateAchievementRate(Long challengeUserId) {
        ChallengeUser challengeUser = challengeUserRepository.findById(challengeUserId)
                .orElseThrow(() -> new RuntimeException("챌린지 유저를 찾을 수 없습니다"));

        Long siteUserId = challengeUser.getSiteUser().getId();
        LocalDate startDate = challengeUser.getChallenge ().getOpenDate ().toLocalDate();
        LocalDate endDate = challengeUser.getEndDate().toLocalDate();

        double attendanceRate = attendanceService.calculateAttendanceRate(siteUserId, startDate, endDate);
        challengeUser.setAchievementRate(attendanceRate);

        challengeUserRepository.save(challengeUser);
    }

    public Challenge create(String name, String description, LocalDateTime startDate, LocalDateTime endDate, int requiredPoint, String type){
        Challenge challenge = new Challenge();

        challenge.setName(name);
        challenge.setDescription(description);
        challenge.setOpenDate(startDate);
        challenge.setCloseDate(endDate);
        challenge.setRequiredPoint(requiredPoint);
        challenge.setType(type);

        return challengeRepository.save(challenge);
    }

    public List<Challenge> getAllChallenges(){
        return challengeRepository.findAll();
    }

    public void participateInChallenge(Long challengeId, Principal principal) {
        String userId = principal.getName();
        Optional<SiteUser> siteUserOptional = siteUserRepository.findByUserId(userId);

        if (siteUserOptional.isPresent()) {
            SiteUser siteUser = siteUserOptional.get();
            Optional<Challenge> challengeOptional = challengeRepository.findById(challengeId);

            if (challengeOptional.isPresent()) {
                Challenge challenge = challengeOptional.get();
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

                    // ChallengeActivity 생성
                    ChallengeActivity activity = new ChallengeActivity();
                    activity.setChallenge(challenge);
                    activity.setActiveDate(LocalDateTime.now());

                    challengeActivityRepository.save(activity);
                } else {
                    challengeUserRepository.delete(optionalChallengeUser.get());
                }
            }
        }
    }
}


