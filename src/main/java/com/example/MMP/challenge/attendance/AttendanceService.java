package com.example.MMP.challenge.attendance;

import com.example.MMP.challenge.challengeUser.ChallengeUser;
import com.example.MMP.challenge.challengeUser.ChallengeUserRepository;
import com.example.MMP.challenge.challengeUser.ChallengeUserService;
import com.example.MMP.security.UserDetail;
import com.example.MMP.siteuser.SiteUser;
import com.example.MMP.siteuser.SiteUserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AttendanceService {
    private static final Logger logger = LoggerFactory.getLogger(AttendanceService.class);

    private final AttendanceRepository attendanceRepository;
    private final SiteUserRepository siteUserRepository;
    private final ChallengeUserRepository challengeUserRepository;

    // 순환 참조 해결 용도
    @Lazy
    @Autowired
    private ChallengeUserService challengeUserService;

    public boolean checkIn(Authentication authentication, Principal principal) {
        UserDetail userDetail = (UserDetail) authentication.getPrincipal();
        LocalDate today = LocalDate.now();

        String userId = principal.getName();
        Optional<SiteUser> optionalSiteUser = siteUserRepository.findByUserId(userId);
        if (!optionalSiteUser.isPresent()) {
            logger.error("User not found with userId: " + userId);
            return false;
        }
        SiteUser siteUser = optionalSiteUser.get();

        if (attendanceRepository.existsBySiteUserIdAndDate(siteUser.getId(), today)) {
            return false;
        }

        Attendance attendance = new Attendance();
        attendance.setSiteUser(siteUser);
        attendance.setDate(today);
        attendance.setPresent(true);

        attendanceRepository.save(attendance);

        // 출석 체크 후 달성률 업데이트
        List<ChallengeUser> challengeUsers = challengeUserRepository.findBySiteUser(siteUser);
        for (ChallengeUser challengeUser : challengeUsers) {
            challengeUserService.updateAchievementRate(challengeUser.getId());
        }

        return true;
    }

    public List<Attendance> getUserAttendance(Long siteUserId) {
        return attendanceRepository.findBySiteUserId(siteUserId);
    }

    public double calculateAttendanceRate(Long siteUserId, LocalDate startDate, LocalDate endDate) {
        List<Attendance> attendanceList = attendanceRepository.findBySiteUserIdAndDateBetween(siteUserId, startDate, endDate);
        long totalDays = ChronoUnit.DAYS.between(startDate, endDate) + 1; // 포함된 총 일수 계산
        return ((double) attendanceList.size() / totalDays) * 100;
    }
}