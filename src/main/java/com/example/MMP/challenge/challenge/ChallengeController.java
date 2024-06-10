package com.example.MMP.challenge.challenge;

import com.example.MMP.attendance.Attendance;
import com.example.MMP.attendance.AttendanceRepository;
import com.example.MMP.challenge.challengeActivity.ChallengeActivity;
import com.example.MMP.challenge.challengeActivity.ChallengeActivityRepository;
import com.example.MMP.challenge.challengeUser.ChallengeUser;
import com.example.MMP.challenge.challengeUser.ChallengeUserRepository;
import com.example.MMP.siteuser.SiteUser;
import com.example.MMP.siteuser.SiteUserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
@RequestMapping("/challenge")
public class ChallengeController {

    private final ChallengeService challengeService;
    private final SiteUserRepository siteUserRepository;
    private final ChallengeUserRepository challengeUserRepository;
    private final ChallengeRepository challengeRepository;
    private final ChallengeActivityRepository challengeActivityRepository;
    private final AttendanceRepository attendanceRepository;

    @Getter
    @Setter
    public class challengeForm {
        @NotNull(message = "제목은 필수로 채워주세요")
        private String name;

        @NotNull(message = "챌린지 설명은 필수로 채워주세요")
        private String description;

        @NotNull(message = "챌린지 시작 날짜는 필수로 채워주세요")
        private LocalDateTime openDate;

        @NotNull(message = "챌린지 종료 날짜는 필수로 채워주세요")
        private LocalDateTime closeDate;

        @NotNull(message = "챌린지 보상은 필수로 채워주세요")
        private int requiredPoint;

        @NotNull(message = "챌린지 타입은 필수로 채워주세요")
        private String type;
    }


    // 챌린지 생성 기능
    @GetMapping("/create")
    public String challengeCreate(Model model) {
        model.addAttribute ("challengeForm", new challengeForm ());
        return "/challenge/challengeCreate_form";
    }

    @PostMapping("/create")
    public String challengeCreate(@Valid @ModelAttribute challengeForm challengeForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors ()) {
            return "challengeForm";
        }
        challengeService.create (challengeForm.getName (), challengeForm.getDescription (), challengeForm.getOpenDate (),
                challengeForm.getCloseDate (), challengeForm.getRequiredPoint (), challengeForm.getType ());
        return "redirect:/challenge/challenges";
    }

    // 챌린지 리스트 기능
    @GetMapping("/challenges")
    public String listChallenges(Model model, Principal principal) {
        List<Long> participatedChallengeIds = new ArrayList<> ();
        List<ChallengeUser> challengeUsers = new ArrayList<> ();

        if (principal != null) {
            String userId = principal.getName ();
            Optional<SiteUser> siteUserOptional = siteUserRepository.findByUserId (userId);
            if (siteUserOptional.isPresent ()) {
                SiteUser siteUser = siteUserOptional.get ();
                challengeUsers = challengeUserRepository.findBySiteUser (siteUser);
                participatedChallengeIds = challengeUsers.stream ()
                        .map (cu -> cu.getChallenge ().getId ())
                        .collect (Collectors.toList ());
            }
        }

        List<Challenge> challenges = challengeRepository.findAll ();
        model.addAttribute ("challenges", challenges);
        model.addAttribute ("participatedChallengeIds", participatedChallengeIds);
        model.addAttribute ("challengeUsers", challengeUsers);
        return "/challenge/challengeList";
    }


    // 챌린지 참여 기능
    @PostMapping("/participate")
    public String participate(@RequestParam("challengeId") Long challengeId, Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/user/login";
        }

        challengeService.participateInChallenge(challengeId, principal);

        // 참여 후 달성률 업데이트
        SiteUser siteUser = siteUserRepository.findByUserId(principal.getName()).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        ChallengeUser challengeUser = challengeUserRepository.findByChallengeAndSiteUser(challengeRepository.findById(challengeId).orElseThrow(() -> new RuntimeException("챌린지를 찾을 수 없습니다.")), siteUser).orElseThrow(() -> new RuntimeException("챌린지 유저를 찾을 수 없습니다."));
        challengeService.updateAchievementRate(challengeUser.getId());

        return "redirect:/challenge/challenges";
    }
}
