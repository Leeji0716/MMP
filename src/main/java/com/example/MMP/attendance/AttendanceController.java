package com.example.MMP.attendance;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/attendance")
public class AttendanceController {
    private final AttendanceService attendanceService;

    @GetMapping("/checkin")
    public String checkInPage() {
        return "/challenge/attendanceCalendar";
    }

    // 출석 체크 처리
    @PostMapping("/checkin")
    public ResponseEntity<String> checkIn(Authentication authentication, Principal principal) {
        boolean success = attendanceService.checkIn(authentication, principal);
        if (success) {
            return ResponseEntity.ok("출석 체크 성공");
        } else {
            return ResponseEntity.badRequest().body("이미 오늘 출석 체크를 하셨습니다.");
        }
    }
    // 출석 캘린더 페이지를 반환
    @GetMapping("/calendar")
    public String getCalendarPage() {
        return "/challenge/attendanceCalendar";
    }

    // 사용자의 출석 기록을 반환
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Attendance>> getUserAttendance(@PathVariable Long userId) {
        List<Attendance> attendanceList = attendanceService.getUserAttendance(userId);
        return ResponseEntity.ok(attendanceList);
    }
}
