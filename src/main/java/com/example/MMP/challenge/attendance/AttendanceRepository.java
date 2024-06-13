package com.example.MMP.challenge.attendance;

import com.example.MMP.siteuser.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance,Long> {
    List<Attendance> findBySiteUserId(Long siteUserId);
    boolean existsBySiteUserIdAndDate(Long siteUserId, LocalDate date);
    List<Attendance> findBySiteUserIdAndDateBetween(Long siteUserId, LocalDate startDate, LocalDate endDate);
    Attendance findByUserAndPresent(SiteUser siteUser, boolean present);
}
