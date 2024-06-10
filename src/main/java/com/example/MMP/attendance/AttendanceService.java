package com.example.MMP.attendance;

import com.example.MMP.security.UserDetail;
import com.example.MMP.siteuser.SiteUser;
import com.example.MMP.siteuser.SiteUserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

        // 출석 체크를 이미 했는지 확인
        if (attendanceRepository.existsBySiteUserIdAndDate(siteUser.getId(), today)) {
            return false;
        }

        Attendance attendance = new Attendance();
        attendance.setSiteUser(siteUser);
        attendance.setDate(today);
        attendance.setPresent(true); // 출석 체크 여부 설정

        attendanceRepository.save(attendance);
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
