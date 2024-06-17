package com.example.MMP.challengeGroup;

import com.example.MMP.siteuser.SiteUser;
import com.example.MMP.siteuser.SiteUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/groupChallenge")
public class ChallengeGroupController {

    private final ChallengeGroupService groupService;
    private final ChallengeGroupRepository groupRepository;
    private final SiteUserService userService;

    @GetMapping("/edit/{groupId}")
    public String editGroup(@PathVariable Long groupId, Model model, Principal principal) {
        Optional<ChallengeGroup> groupOpt = groupRepository.findById(groupId);
        if (groupOpt.isPresent()) {
            ChallengeGroup group = groupOpt.get();
            String username = principal.getName();
            if (!groupService.isLeader(groupId, username)) {
                return "error/403"; // 권한이 부족함을 알리는 적절한 뷰
            }
            model.addAttribute("group", group);
            return "/challenge/groupEdit";
        } else {
            return "error/404";
        }
    }

    @PostMapping("/edit/{groupId}")
    public String updateGroup(@PathVariable Long groupId, @RequestParam String name, @RequestParam String goal, Principal principal) {
        String username = principal.getName();
        if (!groupService.isLeader(groupId, username)) {
            return "error/403"; // 권한이 부족함을 알리는 적절한 뷰
        }
        groupService.updateGroup(groupId, name, goal);
        return "redirect:/groupChallenge/detail/" + groupId;
    }


    @PostMapping("/create")
    public ResponseEntity<ChallengeGroup> createGroup(@RequestParam String name, Principal principal) {
        try {
            ChallengeGroup group = groupService.createGroup(name, principal);
            return ResponseEntity.ok(group);
        } catch (Exception e) {
            // 예외가 발생하면 로그를 남기고 500 에러를 반환
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{groupId}/join")
    public ResponseEntity<Void> joinGroup(@PathVariable Long groupId, @RequestParam Long userId) {
        groupService.addGroup(groupId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list")
    public String getAllGroups(Model model, Principal principal) {
        List<ChallengeGroup> groups = groupService.getAllGroups();
        model.addAttribute("groups", groups);

        // 로그인된 사용자 정보 추가
        String username = principal.getName();
        SiteUser user = userService.getUserByUsername(username);
        model.addAttribute("user", user);

        return "/challenge/groupList_form";
    }

    @GetMapping("/sorted")
    public ResponseEntity<List<ChallengeGroup>> getAllGroupsSortedByMembers() {
        List<ChallengeGroup> groups = groupService.getAllGroupsSortedByMembers();
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/detail/{groupId}")
    public String getGroupDetail(@PathVariable Long groupId, Model model, Principal principal) {
        Optional<ChallengeGroup> groupOpt = groupRepository.findById(groupId);
        if (groupOpt.isPresent()) {
            ChallengeGroup group = groupOpt.get();
            String username = principal.getName();
            boolean isLeader = groupService.isLeader(groupId, username);

            // 멤버들을 가입 날짜와 이름으로 정렬 (null 값을 처리)
            List<SiteUser> sortedMembers = group.getMembers().stream()
                    .sorted(Comparator.comparing(SiteUser::getCreateDate, Comparator.nullsLast(Comparator.naturalOrder()))
                            .thenComparing(SiteUser::getUserId, Comparator.nullsLast(Comparator.naturalOrder())))
                    .collect(Collectors.toList());

            model.addAttribute("group", group);
            model.addAttribute("isLeader", isLeader);
            model.addAttribute("sortedMembers", sortedMembers); // 정렬된 멤버 리스트 추가
            return "/challenge/groupDetail";
        } else {
            return "error/404";
        }
    }

}
