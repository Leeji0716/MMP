package com.example.MMP.challengeGroup;

import com.example.MMP.challenge.attendance.Attendance;
import com.example.MMP.challenge.attendance.AttendanceRepository;
import com.example.MMP.challenge.challenge.Challenge;
import com.example.MMP.chat.ChatRoom;
import com.example.MMP.chat.ChatRoomService;
import com.example.MMP.security.UserDetail;
import com.example.MMP.siteuser.SiteUser;
import com.example.MMP.siteuser.SiteUserRepository;
import com.example.MMP.siteuser.SiteUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/groupChallenge")
public class ChallengeGroupController {

    private final ChallengeGroupService groupService;
    private final ChallengeGroupRepository groupRepository;
    private final SiteUserService userService;
    private final AttendanceRepository attendanceRepository;
    private final SiteUserService siteUserService;
    private final ChatRoomService chatRoomService;
    private final SiteUserRepository siteUserRepository;

    @GetMapping("/edit/{groupId}")
    public String editGroup(@PathVariable Long groupId, Model model, Principal principal) {
        Optional<ChallengeGroup> groupOpt = groupRepository.findById (groupId);
        if (groupOpt.isPresent ()) {
            ChallengeGroup group = groupOpt.get ();
            String username = principal.getName ();
            if (!groupService.isLeader (groupId, username)) {
                return "error/403"; // 권한이 부족함을 알리는 적절한 뷰
            }
            model.addAttribute ("group", group);
            return "challenge/groupEdit";
        } else {
            return "error/404";
        }
    }

    @PostMapping("/edit/{groupId}")
    public String updateGroup(@PathVariable Long groupId, @RequestParam String name, @RequestParam String goal, Principal principal) {
        String username = principal.getName ();
        if (!groupService.isLeader (groupId, username)) {
            return "error/403"; // 권한이 부족함을 알리는 적절한 뷰
        }
        groupService.updateGroup (groupId, name, goal);
        return "redirect:/groupChallenge/detail/" + groupId;
    }


    @PostMapping("/create")
    public ResponseEntity<ChallengeGroup> createGroup(@RequestParam String name, Principal principal) {
        try {
            SiteUser siteUser = siteUserService.getUser(principal.getName());
            ChatRoom chatRoom = new ChatRoom();
            chatRoomService.save(chatRoom);
            chatRoom.getUserList().add(siteUser);
            siteUser.getChatRoomList().add(chatRoom);
            ChallengeGroup group = groupService.createGroup (name, principal,chatRoom);
            return ResponseEntity.ok (group);
        } catch (Exception e) {
            // 예외가 발생하면 로그를 남기고 500 에러를 반환
            e.printStackTrace ();
            return ResponseEntity.status (HttpStatus.INTERNAL_SERVER_ERROR).build ();
        }
    }

    @PostMapping("/{groupId}/join")
    public ResponseEntity<Void> joinGroup(@PathVariable Long groupId, @RequestParam Long userId) {
        groupService.addGroup (groupId, userId);
        return ResponseEntity.ok ().build ();
    }

    @PostMapping("/{groupId}/leave")
    public ResponseEntity<Void> leaveGroup(@PathVariable Long groupId, @RequestParam Long userId) {
        groupService.removeGroup(groupId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list")
    public String getAllGroups(Model model, Principal principal) {
        List<ChallengeGroup> groups = groupService.getAllGroups ();
        model.addAttribute ("groups", groups);

        // 로그인된 사용자 정보 추가
        String username = principal.getName();
        Optional<SiteUser> siteUsers = siteUserRepository.findByUserId (username);

        SiteUser user = siteUsers.get ();

        model.addAttribute ("user", user);

        return "challenge/groupList_form";
    }

    @GetMapping("/sorted")
    public ResponseEntity<List<ChallengeGroup>> getAllGroupsSortedByMembers() {
        List<ChallengeGroup> groups = groupService.getAllGroupsSortedByMembers ();
        return ResponseEntity.ok (groups);
    }

    @GetMapping("/detail/{groupId}")
    public String getGroupDetail(@PathVariable Long groupId, Model model, Principal principal) {
        if (groupId == null || groupId <= 0) {
            return "error/404"; // 잘못된 ID 처리
        }


        Optional<ChallengeGroup> groupOpt = groupRepository.findById (groupId);
        if (groupOpt.isPresent ()) {
            ChallengeGroup group = groupOpt.get ();
            String username = principal.getName ();
            boolean isLeader = groupService.isLeader (groupId, username);

            // 멤버들을 가입 날짜와 이름으로 정렬 (null 값을 처리)
            List<SiteUser> sortedMembers = group.getMembers ().stream ()
                    .sorted (Comparator.comparing (SiteUser::getCreateDate, Comparator.nullsLast (Comparator.naturalOrder ()))
                            .thenComparing (SiteUser::getUserId, Comparator.nullsLast (Comparator.naturalOrder ())))
                    .collect (Collectors.toList ());

            // 해당 그룹의 출석 기록 조회

            List<Attendance> attendances = attendanceRepository.findByChallengeGroupId (groupId);

            // 각 멤버별 총 출석 시간 계산 및 포맷팅
            Map<Long, String> memberAttendanceFormattedMap = new HashMap<> ();
            Map<Long, Long> memberAttendanceTimeMap = attendances.stream ()
                    .collect (Collectors.groupingBy (att -> att.getSiteUser ().getId (),
                            Collectors.summingLong (Attendance::getTotalTime)));

            memberAttendanceTimeMap.forEach ((memberId, totalSeconds) -> {
                long hours = totalSeconds / 3600;
                long minutes = (totalSeconds % 3600) / 60;
                long seconds = totalSeconds % 60;
                String formattedTime = String.format ("%d 시간 %d 분 %d 초", hours, minutes, seconds);
                memberAttendanceFormattedMap.put (memberId, formattedTime);
            });

            // 모든 그룹의 총 운동 시간과 순위를 계산
            Map<Long, Integer> groupRanks = groupService.getGroupRanks (memberAttendanceTimeMap);
            int groupRank = groupRanks.get (group.getId ());

            model.addAttribute ("group", group);
            model.addAttribute ("isLeader", isLeader);
            model.addAttribute ("sortedMembers", sortedMembers); // 정렬된 멤버 리스트 추가
            model.addAttribute ("memberAttendanceFormattedMap", memberAttendanceFormattedMap); // 멤버별 포맷된 출석 시간 추가
            model.addAttribute ("groupRank", groupRank); // 그룹 순위 추가
            model.addAttribute ("groupLeader",group.getLeader ());

            return "challenge/groupDetail";
        } else {
            return "error/404"; // 그룹을 찾지 못한 경우
        }
    }

    @GetMapping("/groupTalk/{id}")
    public String groupTalk(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetail userDetail, Model model){
        SiteUser siteUser = siteUserService.getUser(userDetail.getUsername());
        ChallengeGroup challengeGroup = groupService.getGroup(id);
        model.addAttribute("challengeGroup",challengeGroup);
        model.addAttribute("me",siteUser);
        return "chat/groupchat";
    }

    @PostMapping("/delete/{groupId}")
    public String challengeGroupDelete(@PathVariable Long groupId) {
        ChallengeGroup challengeGroup = groupService.getGroup(groupId);
        groupService.deleteGroup(challengeGroup);
        return "redirect:/groupChallenge/list";
    }
}


