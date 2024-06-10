package com.example.MMP.challenge.challenge;

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
        model.addAttribute("challengeForm", new challengeForm ());
        return "/challenge/challengeCreate_form";
    }

    @PostMapping("/create")
    public String challengeCreate(@Valid @ModelAttribute challengeForm challengeForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "challengeForm";
        }
        challengeService.create(challengeForm.getName(), challengeForm.getDescription(), challengeForm.getOpenDate(),
                challengeForm.getCloseDate(), challengeForm.getRequiredPoint(), challengeForm.getType());
        return "redirect:/challenge/challenges";
    }

    // 챌린지 리스트 기능
    @GetMapping("/challenges")
    public String listChallenges(Model model, Principal principal) {
        List<Long> participatedChallengeIds = new ArrayList<>();
        List<ChallengeUser> challengeUsers = new ArrayList<> ();

        if (principal != null) {
            String userId = principal.getName();
            Optional<SiteUser> siteUserOptional = siteUserRepository.findByUserId(userId);
            if (siteUserOptional.isPresent()) {
                SiteUser siteUser = siteUserOptional.get();
                challengeUsers = challengeUserRepository.findBySiteUser(siteUser);
                participatedChallengeIds = challengeUsers.stream()
                        .map(cu -> cu.getChallenge().getId())
                        .collect(Collectors.toList());
            }
        }

        List<Challenge> challenges = challengeRepository.findAll();
        model.addAttribute("challenges", challenges);
        model.addAttribute("participatedChallengeIds", participatedChallengeIds);
        model.addAttribute("challengeUsers", challengeUsers);
        return "/challenge/challengeList";
    }


    // 챌린지 참여 기능
    @PostMapping("/participate")
    public String participate(@RequestParam("challengeId") Long challengeId, Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/user/login";
        }

        String userId = principal.getName();
        Optional<SiteUser> siteUserOptional = siteUserRepository.findByUserId(userId);

        if (siteUserOptional.isPresent()) {
            SiteUser siteUser = siteUserOptional.get();
            Optional<Challenge> challengeOptional = challengeRepository.findById(challengeId);

            if (challengeOptional.isPresent()) {
                Challenge challenge = challengeOptional.get();
                Optional<ChallengeUser> optionalChallengeUser = challengeUserRepository.findByChallengeAndSiteUser(challenge, siteUser);

                if (optionalChallengeUser.isEmpty()) {
                    // ChallengeUser 생성
                    ChallengeUser challengeUser = new ChallengeUser();
                    challengeUser.setChallenge(challenge);
                    challengeUser.setSiteUser(siteUser);
                    challengeUser.setStartDate(LocalDateTime.now());
                    challengeUser.setEndDate(challenge.getCloseDate());
                    challengeUser.setSuccess(false);
                    challengeUserRepository.save(challengeUser);

                    // ChallengeActivity 생성
                    ChallengeActivity activity = new ChallengeActivity ();
                    activity.setChallenge(challenge);
                    activity.setActiveDate(LocalDateTime.now());

                    switch (challenge.getType()) {
                        case "weight":
                            activity.setWeight(0); // 초기 값 설정, 나중에 업데이트 필요
                            break;
                        case "exerciseTime":
                            activity.setExerciseTime(0); // 초기 값 설정, 나중에 업데이트 필요
                            break;
                        case "attendance":
                            activity.setAttendance(null); // 초기 값 설정, 나중에 업데이트 필요
                            break;
                    }

                    challengeActivityRepository.save(activity);
                } else {
                    challengeUserRepository.delete(optionalChallengeUser.get());
                }
            }
        }

        return "redirect:/challenge/challenges";
    }

}
