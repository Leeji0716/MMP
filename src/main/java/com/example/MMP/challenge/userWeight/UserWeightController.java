package com.example.MMP.challenge.userWeight;

import com.example.MMP.security.UserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class UserWeightController {
    private final UserWeightService userWeightService;

    @GetMapping("/weight")
    public String weightForm(Model model) {
        return "challenge/weightForm";
    }

    @PostMapping("/weight")
    public String recordWeight(@RequestParam("weight") double weight, Authentication authentication) {
        UserDetail userDetail = (UserDetail) authentication.getPrincipal();
        userWeightService.recordWeight(userDetail.getId(), weight);
        return "redirect:/weight";
    }
}
