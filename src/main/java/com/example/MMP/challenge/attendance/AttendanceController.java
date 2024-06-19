package com.example.MMP.challenge.attendance;

import com.example.MMP.challenge.challenge.ChallengeRepository;
import com.example.MMP.challenge.challenge.ChallengeService;
import com.example.MMP.challenge.challengeUser.ChallengeUser;
import com.example.MMP.challenge.challengeUser.ChallengeUserRepository;
import com.example.MMP.security.UserDetail;
import com.example.MMP.siteuser.SiteUser;
import com.example.MMP.siteuser.SiteUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/attendance")
public class AttendanceController {
    private final AttendanceService attendanceService;
    private final SiteUserService siteUserService;
    private final ChallengeUserRepository challengeUserRepository;
    private final ChallengeRepository challengeRepository;
    private final ChallengeService challengeService;

    @GetMapping("/checkin")
    public String checkInPage() {
        return "challenge/attendanceCalendar";
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

        // 시작 시간과 종료 시간 추가
        LocalDateTime startTime = attendanceService.getStartTime(userId);
        LocalDateTime endTime = attendanceService.getEndTime(userId);

        // Distinct attendance count 추가
        Long distinctAttendanceCount = attendanceService.countDistinctAttendanceDates(userId);

        long totalExerciseTime = attendanceService.calculateTotalExerciseTime(userId);

        model.addAttribute("attendanceList", attendanceList);
        model.addAttribute("startTime", startTime);
        model.addAttribute("endTime", endTime);
        model.addAttribute("distinctAttendanceCount", distinctAttendanceCount);
        model.addAttribute("totalExerciseTime", totalExerciseTime);

        return "challenge/attendanceCalendar";
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

        SiteUser siteUser = siteUserService.getUser(userId);
        List<ChallengeUser> challengeUsers = challengeUserRepository.findBySiteUser(siteUser);

        if (challengeUsers.isEmpty()) {
            return ResponseEntity.ok(result); // 챌린지 유저가 없으면 결과 반환
        } else {
            for (ChallengeUser challengeUser : challengeUsers) {
                if (challengeUser.getInitialExerciseTime() != null) {
                    Long challengeId = challengeUser.getChallenge().getId();
                    Boolean expired = challengeRepository.findById(challengeId).get().isExpiration();
                    if (expired) {
                        continue;
                    } else {
                        challengeService.updateExerciseTime(challengeId, principal);
                    }
                }
            }
        }
        return ResponseEntity.ok(result);
    }
}