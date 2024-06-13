package com.example.MMP.challenge.attendance;

import com.example.MMP.siteuser.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance,Long> {
    List<Attendance> findBySiteUserId(Long siteUserId);
    boolean existsBySiteUserIdAndDate(Long siteUserId, LocalDate date);
    List<Attendance> findBySiteUserIdAndDateBetween(Long siteUserId, LocalDate startDate, LocalDate endDate);
    Optional<Attendance> findFirstBySiteUserAndPresent(SiteUser siteUser, boolean present);
}
