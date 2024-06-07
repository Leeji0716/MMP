package com.example.MMP.challenge.challengeUser;


import com.example.MMP.attendance.AttendanceService;
import com.example.MMP.challenge.challenge.ChallengeRepository;
import com.example.MMP.siteuser.SiteUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/challenge")
public class ChallengeUserController {

    private final SiteUserRepository siteUserRepository;
    private final ChallengeUserRepository challengeUserRepository;
    private final AttendanceService attendanceService;
    private final ChallengeRepository challengeRepository;



}
