package com.example.MMP.challenge.attendance;


import com.example.MMP.challenge.challengeUser.ChallengeUser;
import com.example.MMP.challenge.challengeUser.ChallengeUserRepository;
import com.example.MMP.challenge.challengeUser.ChallengeUserService;
import com.example.MMP.challengeGroup.ChallengeGroup;
import com.example.MMP.challengeGroup.ChallengeGroupRepository;
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
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AttendanceService {
    private static final Logger logger = LoggerFactory.getLogger (AttendanceService.class);

    private final AttendanceRepository attendanceRepository;
    private final SiteUserRepository siteUserRepository;
    private final ChallengeUserRepository challengeUserRepository;
    private final ChallengeGroupRepository challengeGroupRepository;



    // 순환 참조 해결 용도
    @Lazy
    @Autowired
    private ChallengeUserService challengeUserService;

    public boolean checkIn(Authentication authentication, Principal principal) {
        UserDetail userDetail = (UserDetail) authentication.getPrincipal ();
        LocalDate today = LocalDate.now ();

        String userId = principal.getName ();
        Optional<SiteUser> optionalSiteUser = siteUserRepository.findByUserId (userId);
        if (!optionalSiteUser.isPresent ()) {
            logger.error ("유저를 찾을 수 없습니다. " + userId);
            return false;
        }
        SiteUser siteUser = optionalSiteUser.get ();

        if (attendanceRepository.existsBySiteUserIdAndDate (siteUser.getId (), today)) {
            return false;
        }

        // 출석 체크 후 달성률 업데이트
        List<ChallengeUser> challengeUsers = challengeUserRepository.findBySiteUser (siteUser);
        for (ChallengeUser challengeUser : challengeUsers) {
            challengeUserService.updateAchievementRate (challengeUser.getId ());
        }

        return true;
    }

    public LocalDateTime getStartTime(Long userId){
        return attendanceRepository.findStartTimeByUserId (userId);
    }
    public LocalDateTime getEndTime(Long userId){
        return attendanceRepository.findEndTimeByUserId (userId);
    }

    public Integer getTotalTime(Long userId) {
        Integer totalTime = attendanceRepository.findTotalTimeByUserId(userId);
        return totalTime != null ? totalTime : 0; // null 처리
    }

    public List<Attendance> getUserAttendance(Long siteUserId) {
        return attendanceRepository.findBySiteUserId (siteUserId);
    }

    public Long countDistinctAttendanceDates(Long userId) {
        return attendanceRepository.countDistinctBySiteUserId(userId);
    }

    public double calculateAttendanceRate(Long siteUserId, LocalDate startDate, LocalDate endDate) {
        List<Attendance> attendanceList = attendanceRepository.findBySiteUserIdAndDateBetween (siteUserId, startDate, endDate);
        long totalDays = ChronoUnit.DAYS.between (startDate, endDate) + 1; // 포함된 총 일수 계산
        return ((double) attendanceList.size () / totalDays) * 100;
    }

    public String handleEntry(String userId, String action) {
        Optional<SiteUser> _siteUser = siteUserRepository.findByUserId(userId);
        if (_siteUser.isEmpty()) {
            return "유저를 찾을 수 없습니다.";
        }
        SiteUser siteUser = _siteUser.get();

        if ("enter".equals(action)) {
            Attendance attendance = new Attendance();
            attendance.setSiteUser(siteUser);
            attendance.setPresent(true);
            attendance.setStartTime(LocalDateTime.now());
            attendance.setDate(LocalDate.now());

            // ChallengeGroup을 조회하고 존재하면 설정
            List<ChallengeGroup> challengeGroups = challengeGroupRepository.findByMembersContaining(siteUser);
            if (!challengeGroups.isEmpty()) {
                attendance.setChallengeGroup(challengeGroups.get(0)); // 첫 번째 ChallengeGroup을 설정
            }

            attendanceRepository.save(attendance);
            return "입장 완료 되었습니다.";
        } else if ("exit".equals(action)) {
            Optional<Attendance> optionalAttendance = attendanceRepository.findFirstBySiteUserAndPresent(siteUser, true);
            if (optionalAttendance.isEmpty()) {
                return "현재 입장 중이 아닙니다.";
            } else {
                Attendance attendance = optionalAttendance.get();
                attendance.setPresent(false);
                attendance.setEndTime(LocalDateTime.now());
                attendanceRepository.save(attendance);
                return "퇴실이 완료 되었습니다.";
            }
        } else {
            return "잘못된 동작입니다.";
        }
    }

    public boolean isUserPresent(Long siteUserId) {
        Optional<Attendance> optionalAttendance = attendanceRepository.findFirstBySiteUserAndPresent(siteUserRepository.findById(siteUserId).orElse(null), true);
        return optionalAttendance.isPresent();
    }

    public long calculateTotalExerciseTime(Long userId) {
        List<Attendance> attendanceList = attendanceRepository.findBySiteUserId(userId);

        return attendanceList.stream()
                .filter(attendance -> attendance.getStartTime() != null && attendance.getEndTime() != null)
                .mapToLong(attendance -> ChronoUnit.MINUTES.between(attendance.getStartTime(), attendance.getEndTime()))
                .sum();
    }

    // 사용자의 특정 날짜 범위 내 운동 시간 계산
    public long calculateTotalExerciseTimeBetweenDates(Long userId, LocalDate startDate, LocalDate endDate) {
        List<Attendance> attendanceList = attendanceRepository.findBySiteUserIdAndDateBetween(userId, startDate, endDate);

        return attendanceList.stream()
                .filter(attendance -> attendance.getStartTime() != null && attendance.getEndTime() != null)
                .mapToLong(attendance -> ChronoUnit.MINUTES.between(attendance.getStartTime(), attendance.getEndTime()))
                .sum();
    }

    public List<Attendance> getAttendanceByUserIdAndStartDate(Long userId, LocalDateTime startDate) {
        return attendanceRepository.findBySiteUserIdAndDateAfter(userId, startDate.toLocalDate());
    }
}
