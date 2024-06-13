package com.example.MMP.challenge.attendance;

import com.example.MMP.security.UserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<String> checkIn(Authentication authentication,
                                          Principal principal) {
        boolean success = attendanceService.checkIn(authentication, principal);
        if (success) {
            return ResponseEntity.ok("출석 체크 성공");
        } else {
            return ResponseEntity.badRequest().body("이미 오늘 출석 체크를 하셨습니다.");
        }
    }

    // 출석 캘린더 페이지를 반환
    @GetMapping("/calendar")
    public String getCalendarPage(Authentication authentication, Model model) {
        UserDetail userDetail = (UserDetail) authentication.getPrincipal();
        Long userId = userDetail.getId(); // 사용자 ID를 가져옵니다.
        List<Attendance> attendanceList = attendanceService.getUserAttendance(userId);
        model.addAttribute("attendanceList", attendanceList);
        return "/challenge/attendanceCalendar";
    }

    // 사용자의 출석 기록을 반환
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Attendance>> getUserAttendance(@PathVariable Long userId) {
        List<Attendance> attendanceList = attendanceService.getUserAttendance(userId);
        return ResponseEntity.ok(attendanceList);
    }

    @GetMapping("/myAttendance")
    public ResponseEntity<List<Attendance>> getCurrentUserAttendance(Authentication authentication) {
        UserDetail userDetail = (UserDetail) authentication.getPrincipal();
        Long userId = userDetail.getId(); // 사용자 ID를 가져옵니다.
        List<Attendance> attendanceList = attendanceService.getUserAttendance(userId);
        return ResponseEntity.ok(attendanceList);
    }

    @PostMapping("/enter")
    public ResponseEntity<String> enter(Authentication authentication, Principal principal) {
        UserDetail userDetail = (UserDetail) authentication.getPrincipal();
        String userId = principal.getName();
        String result = attendanceService.handleEntry(userId, "enter");
        return ResponseEntity.ok(result);
    }

    @PostMapping("/exit")
    public ResponseEntity<String> exit(Authentication authentication, Principal principal) {
        UserDetail userDetail = (UserDetail) authentication.getPrincipal();
        String userId = principal.getName();
        String result = attendanceService.handleEntry(userId, "exit");
        return ResponseEntity.ok(result);
    }

    @PostMapping("/bluetooth-entry")
    public ResponseEntity<String> bluetoothEntry(@RequestBody Map<String, String> payload) {
        String userId = payload.get("userId");
        String action = payload.get("action");

        if (userId == null || action == null) {
            return ResponseEntity.badRequest().body("Invalid request data");
        }

        String result = attendanceService.handleEntry(userId, action);
        return ResponseEntity.ok(result);
    }

}