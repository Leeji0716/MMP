package com.example.MMP.challenge.challenge;

import com.example.MMP.challenge.challengeUser.ChallengeUserRepository;
import com.example.MMP.challenge.challengeUser.challengeUser;
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
    public String listChallenges(Model model) {
        List<Challenge> challenges = challengeService.getAllChallenges();
        model.addAttribute("challenges", challenges);
        return "/challenge/challengeList";
    }

    @GetMapping("/list")
    public String list(Model model, Principal principal) {
        if (principal != null) {
            String userId = principal.getName();
            Optional<SiteUser> siteUserOptional = siteUserRepository.findByUserId(userId);
            if (siteUserOptional.isPresent()) {
                SiteUser siteUser = siteUserOptional.get();
                List<challengeUser> challengeUsers = challengeUserRepository.findBySiteUser(siteUser);
                List<Long> participatedChallengeIds = challengeUsers.stream()
                        .map(cu -> cu.getChallenge().getId())
                        .collect(Collectors.toList());
                model.addAttribute("participatedChallengeIds", participatedChallengeIds);
            }
        }

        List<Challenge> challenges = challengeRepository.findAll();
        model.addAttribute("challenges", challenges);
        return "/challenge/challengeList";
    }

    // 챌린지 참여 기능
    @PostMapping("/participate")
    public String participate(@RequestParam("challengeId") Long challengeId, Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }

        String userId = principal.getName(); // 현재 로그인한 사용자의 userId 가져오기
        Optional<SiteUser> siteUserOptional = siteUserRepository.findByUserId(userId);

        if (siteUserOptional.isPresent()) {
            SiteUser siteUser = siteUserOptional.get();
            Optional<Challenge> challengeOptional = challengeRepository.findById(challengeId);

            if (challengeOptional.isPresent()) {
                Challenge challenge = challengeOptional.get();
                Optional<challengeUser> optionalChallengeUser = challengeUserRepository.findByChallengeAndSiteUser(challenge, siteUser);

                if (optionalChallengeUser.isEmpty()) {
                    // 사용자가 아직 이 챌린지에 참여하지 않았다면 새로 추가
                    challengeUser challengeUser = new challengeUser();
                    challengeUser.setChallenge(challenge);
                    challengeUser.setSiteUser(siteUser);
                    challengeUser.setStartDate(LocalDateTime.now()); // 시작 날짜 설정
                    challengeUser.setEndDate(challenge.getCloseDate()); // 종료 날짜 설정
                    challengeUser.setSuccess(false); // 초기 성공 여부 설정
                    challengeUserRepository.save(challengeUser);
                } else {
                    // 이미 참여한 경우 해당 레코드 삭제 (포기)
                    challengeUserRepository.delete(optionalChallengeUser.get());
                }
            }
        }

        return "redirect:/challenge/list";
    }

}
