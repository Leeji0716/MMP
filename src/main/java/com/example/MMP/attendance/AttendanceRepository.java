package com.example.MMP.attendance;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance,Long> {
    List<Attendance> findBySiteUserId(Long siteUserId);
    boolean existsBySiteUserIdAndDate(Long siteUserId, LocalDate date);
    List<Attendance> findBySiteUserIdAndDateBetween(Long siteUserId, LocalDate startDate, LocalDate endDate);
}
