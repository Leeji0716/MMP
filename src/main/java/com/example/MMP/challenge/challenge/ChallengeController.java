package com.example.MMP.challenge.challenge;

import com.example.MMP.challenge.challengeUser.ChallengeUser;
import com.example.MMP.challenge.challengeUser.ChallengeUserRepository;
import com.example.MMP.siteuser.SiteUser;
import com.example.MMP.siteuser.SiteUserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
@RequestMapping("/challenge")
public class ChallengeController {

    private final ChallengeService challengeService;
    private final SiteUserRepository siteUserRepository;
    private final ChallengeUserRepository challengeUserRepository;
    private final ChallengeRepository challengeRepository;

    @Getter
    @Setter
    public static class ChallengeForm {
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

        // 몸무게 챌린지일 때만 사용
        private Double targetWeightLoss;

        // 운동시간 챌린지일 때만 사용
        private Integer targetExerciseMinutes;
    }

    // 챌린지 생성 기능
    @GetMapping("/create")
    public String challengeCreate(Model model) {
        model.addAttribute("challengeForm", new ChallengeForm());
        return "challenge/challengeCreate_form";
    }

    @PostMapping("/create")
    public String challengeCreate(@Valid @ModelAttribute ChallengeForm challengeForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "challengeForm";
        }
        challengeService.create(
                challengeForm.getName(),
                challengeForm.getDescription(),
                challengeForm.getOpenDate(),
                challengeForm.getCloseDate(),
                challengeForm.getRequiredPoint(),
                challengeForm.getType(),
                challengeForm.getTargetWeightLoss(),
                challengeForm.getTargetExerciseMinutes()
        );
        return "redirect:/challenge/challenges";
    }

    @PostMapping("/delete/{id}")
    public String challengeDelete(@PathVariable("id") Long challengeId) {
        Challenge challenge = challengeService.getChallenge(challengeId);
        challengeService.delete(challenge);
        return "redirect:/challenge/challenges";
    }

    // 챌린지 리스트 기능
    @GetMapping("/challenges")
    public String listChallenges(Model model, Principal principal) {
        List<Long> participatedChallengeIds = new ArrayList<>();
        List<ChallengeUser> challengeUsers = new ArrayList<>();
        Map<Long, Double> challengeAchievementRates = new HashMap<> ();



        if (principal != null) {
            String userId = principal.getName();
            Optional<SiteUser> siteUserOptional = siteUserRepository.findByUserId(userId);
            if (siteUserOptional.isPresent()) {
                SiteUser siteUser = siteUserOptional.get();

                challengeUsers = challengeUserRepository.findBySiteUser(siteUser);
                participatedChallengeIds = challengeUsers.stream()
                        .map(cu -> cu.getChallenge().getId())
                        .collect(Collectors.toList());
                for (ChallengeUser challengeUser : challengeUsers) {
                    challengeAchievementRates.put(challengeUser.getChallenge().getId(), challengeUser.getAchievementRate());
                }
            }
        }
        List<Challenge> challenges = challengeRepository.findAll();
        LocalDateTime now = LocalDateTime.now();
        for (Challenge challenge : challenges) {
            if (challenge.getCloseDate().isBefore(now) && !challenge.isExpiration ()) {
                challengeService.expiration(challenge.getId());
            }
        }

        model.addAttribute("challenges", challenges);
        model.addAttribute("participatedChallengeIds", participatedChallengeIds);
        model.addAttribute("challengeUsers", challengeUsers);
        model.addAttribute("challengeAchievementRates", challengeAchievementRates);

        // 현재 날짜를 모델에 추가
        model.addAttribute("currentDate", LocalDate.now());
        return "challenge/challengeList";
    }


    // 챌린지 참여 기능 (POST)
    @PostMapping("/participate")
    public String participate(@RequestParam("challengeId") Long challengeId, Principal principal) {
        if (principal == null) {
            return "redirect:/user/login";
        }

        Optional<Challenge> challengeOptional = challengeRepository.findById(challengeId);
        if (challengeOptional.isPresent()) {
            Challenge challenge = challengeOptional.get();
            switch (challenge.getType()) {
                case "weight":
                    return "redirect:/challenge/enterWeight?challengeId=" + challengeId;
                case "exerciseTime":
                    // 초기 운동 시간을 0으로 설정
                    challengeService.participateInChallengeWithExerciseTime(challengeId, principal, 0);
                    break;
                case "attendance":
                    challengeService.participateInChallenge (challengeId,principal);
                    break;
                default:
                    challengeService.participateInChallenge(challengeId, principal);
                    break;
            }
        }
        return "redirect:/challenge/challenges";
    }

    @PostMapping("/expiration")
    private String expiration(@RequestParam("challengeId") Long challengeId){
        challengeService.expiration (challengeId);

        return "redirect:/challenge/challenges";
    }


    @GetMapping("/enterWeight")
    public String enterWeightForm(@RequestParam("challengeId") Long challengeId, Model model) {
        model.addAttribute("challengeId", challengeId);
        return "challenge/enterWeight";
    }

    @PostMapping("/enterWeight")
    public String enterWeight(@RequestParam("challengeId") Long challengeId, @RequestParam("weight") double weight, Principal principal) {
        if (principal == null) {
            return "redirect:/user/login";
        }

        challengeService.participateInChallengeWithWeight(challengeId, principal, weight);
        return "redirect:/challenge/challenges";
    }


    // 변경된 몸무게 입력 폼 반환
    @GetMapping("/updateWeight")
    public String updateWeightForm(@RequestParam("challengeId") Long challengeId, Model model) {
        model.addAttribute("challengeId", challengeId);
        return "challenge/weightForm";
    }

    // 변경된 몸무게 입력 처리
    @PostMapping("/updateWeight")
    public String updateWeight(@RequestParam("challengeId") Long challengeId, @RequestParam("weight") double weight, Principal principal) {
        if (principal == null) {
            return "redirect:/user/login";
        }

        challengeService.updateWeight(challengeId, principal, weight);
        return "redirect:/challenge/challenges";
    }

}