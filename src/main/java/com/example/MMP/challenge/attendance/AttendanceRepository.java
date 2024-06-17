package com.example.MMP.challenge.attendance;

import com.example.MMP.siteuser.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance,Long> {
    List<Attendance> findBySiteUserId(Long siteUserId);

    boolean existsBySiteUserIdAndDate(Long siteUserId, LocalDate date);

    List<Attendance> findBySiteUserIdAndDateBetween(Long siteUserId, LocalDate startDate, LocalDate endDate);

    Optional<Attendance> findFirstBySiteUserAndPresent(SiteUser siteUser, boolean present);

    @Query("SELECT a.startTime FROM Attendance a WHERE a.siteUser.id = :userId ORDER BY a.startTime ASC LIMIT 1")
    LocalDateTime findStartTimeByUserId(@Param("userId") Long userId);

    @Query("SELECT a.endTime FROM Attendance a WHERE a.siteUser.id = :userId ORDER BY a.endTime DESC LIMIT 1")
    LocalDateTime findEndTimeByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(DISTINCT a.date) FROM Attendance a WHERE a.siteUser.id = :userId")
    Long countDistinctBySiteUserId(@Param("userId") Long userId);

    List<Attendance> findByChallengeGroupId(Long groupId);

}
