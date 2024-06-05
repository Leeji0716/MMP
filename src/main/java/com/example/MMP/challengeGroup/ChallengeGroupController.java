package com.example.MMP.challengeGroup;

import com.example.MMP.siteuser.SiteUser;
import com.example.MMP.siteuser.SiteUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/groupChallenge")
public class ChallengeGroupController {

    private final ChallengeGroupService challengeGroupService;
    private final SiteUserService siteUserService;

    @GetMapping("/list")
    public String getAllGroups(Model model, Principal principal) {
        List<ChallengeGroup> groups = challengeGroupService.getAllGroups();
        model.addAttribute("groups", groups);

        // 로그인된 사용자 정보 추가
        String username = principal.getName();
        SiteUser user = siteUserService.getUserByUsername (username);
        model.addAttribute("user", user);

        return "/challenge/groupChallengeList";
    }
}
