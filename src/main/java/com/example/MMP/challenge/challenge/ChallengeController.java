package com.example.MMP.challenge.challenge;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/challenge")
public class ChallengeController {

    private final ChallengeService challengeService;

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

    // 챌린지 가입 기능


}
