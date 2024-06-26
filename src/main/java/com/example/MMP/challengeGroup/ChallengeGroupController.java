package com.example.MMP.challengeGroup;

import com.example.MMP.Tag.Tag;
import com.example.MMP.Tag.TagRepository;
import com.example.MMP.challenge.attendance.Attendance;
import com.example.MMP.challenge.attendance.AttendanceRepository;
import com.example.MMP.challenge.challengeUser.ChallengeUser;
import com.example.MMP.challengeGroup.GroupTag.GroupTagRepository;
import com.example.MMP.challengeGroup.GroupTag.GroupTagService;
import com.example.MMP.chat.ChatMessageService;
import com.example.MMP.chat.ChatRoom;
import com.example.MMP.chat.ChatRoomService;
import com.example.MMP.security.UserDetail;
import com.example.MMP.siteuser.SiteUser;
import com.example.MMP.siteuser.SiteUserRepository;
import com.example.MMP.siteuser.SiteUserService;
import jakarta.persistence.Table;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
    private final ChatMessageService chatMessageService;
    private final GroupTagService tagService;
    private final TagRepository tagRepository;

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
            SiteUser siteUser = siteUserService.getUser (principal.getName ());
            ChatRoom chatRoom = new ChatRoom ();
            chatRoom.setSort ("many");
            chatRoomService.save (chatRoom);
            chatRoom.getUserList ().add (siteUser);
            siteUser.getChatRoomList ().add (chatRoom);
            ChallengeGroup group = groupService.createGroup (name, principal, chatRoom);
            chatMessageService.firstGroupChatMessage (siteUser, chatRoom, group.getName ());
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
        // 만약 리더가 탈퇴를 하려고 하면 그룹 삭제가 되는 기능 구현해야 돼 !


        groupService.removeGroup (groupId, userId);
        return ResponseEntity.ok ().build ();
    }

    @GetMapping("/list")
    public String getAllGroups(Model model, Principal principal) {

        List<ChallengeGroup> groups = groupService.getGroupRanks ();
        // 로그인된 사용자 정보 추가
        String username = principal.getName ();
        Long id = siteUserService.findByUserName (username).getId ();
        Optional<SiteUser> siteUsers = siteUserRepository.findByUserId (username);
        SiteUser user = siteUsers.orElse (null);

        List<Boolean> leaderStatus = new ArrayList<> ();

        for (ChallengeGroup challengeGroup : groups) {
            boolean isCurrentUserLeader = challengeGroup.getLeader ().getId () != null && challengeGroup.getLeader ().getId ().equals (id);
            leaderStatus.add (isCurrentUserLeader);
        }

        model.addAttribute ("groups", groups);
        model.addAttribute ("leaderStatus", leaderStatus);
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

        List<ChallengeGroup> challengeGroups = groupService.getGroupRanks ();
        ChallengeGroup target = null;
        for (ChallengeGroup challengeGroup : challengeGroups) {
            if (challengeGroup.getId () == groupId) {
                target = challengeGroup;
            }
        }

        if (target != null) {
            ChallengeGroup group = target;
            String username = principal.getName ();
            boolean isLeader = groupService.isLeader (groupId, username);

            // 멤버들을 가입 날짜와 이름으로 정렬 (null 값을 처리)
            List<SiteUser> sortedMembers = group.getMembers ().stream ()
                    .sorted (Comparator.comparing (SiteUser::getCreateDate, Comparator.nullsLast (Comparator.naturalOrder ()))
                            .thenComparing (SiteUser::getUserId, Comparator.nullsLast (Comparator.naturalOrder ())))
                    .collect (Collectors.toList ());


// 리더를 맨 위로 올리기 위해서 멤버 리스트에서 리더를 분리하고 맨 앞에 추가
            SiteUser leader = group.getLeader ();
            sortedMembers.removeIf (member -> member.getId ().equals (leader.getId ()));
            sortedMembers.add (0, leader);

            // 모든 출석 기록을 포함할 리스트 초기화
            List<Attendance> allAttendances = new ArrayList<> ();

            // 각 멤버별 출석 기록 조회 및 추가
            for (SiteUser siteUser : sortedMembers) {
                List<Attendance> attendances = attendanceRepository.findBySiteUserId (siteUser.getId ());
                allAttendances.addAll (attendances); // 조회된 출석 기록을 전체 리스트에 추가
            }

            Map<Long, Long> totalTimeByMember = allAttendances.stream ()
                    .collect (Collectors.groupingBy (
                            attendance -> attendance.getSiteUser ().getId (),
                            Collectors.summingLong (Attendance::getTotalTime)
                    ));

            List<Tag> tags = tagRepository.findAll ();


            model.addAttribute ("tags", tags);
            model.addAttribute ("group", group);
            model.addAttribute ("isLeader", isLeader);
            model.addAttribute ("sortedMembers", sortedMembers); // 정렬된 멤버 리스트 추가
            model.addAttribute ("groupLeader", group.getLeader ());
            model.addAttribute ("totalTimeByMember", totalTimeByMember);

            return "challenge/groupDetail";
        } else {
            return "error/404"; // 그룹을 찾지 못한 경우
        }
    }

    @GetMapping("/groupTalk/{id}")
    public String groupTalk(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetail userDetail, Model model) {
        SiteUser siteUser = siteUserService.getUser (userDetail.getUsername ());
        ChallengeGroup challengeGroup = groupService.getGroup (id);
        List<SiteUser> memberList = new ArrayList<> (challengeGroup.getMembers ());
        List<String> memberNumber = new ArrayList<> ();
        for (SiteUser siteUser1 : memberList) {
            memberNumber.add (siteUser1.getNumber ());
        }
        ChatRoom chatRoom = chatRoomService.findById (challengeGroup.getChatRoom ().getId ());

        chatRoomService.deleteGroupAlarm (challengeGroup, siteUser);
        model.addAttribute ("challengeGroup", challengeGroup);
        model.addAttribute ("me", siteUser);
        model.addAttribute ("chatRoom", chatRoom);
        model.addAttribute ("memberNumber", memberNumber);


        return "chat/groupchat";
    }

    @Transactional
    @PostMapping("/delete/{groupId}")
    public String challengeGroupDelete(@PathVariable Long groupId) {
        ChallengeGroup challengeGroup = groupService.getGroup (groupId);
        groupService.deleteGroup (challengeGroup.getId ());
        return "redirect:/groupChallenge/list";
    }

    @PostMapping("/changeLeader")
    public String changeLeader(@RequestParam("groupId") Long groupId, @RequestParam("newLeaderId") Long newLeaderId) {
        groupService.changeLeader (groupId, newLeaderId);
        return "redirect:/groupChallenge/detail/" + groupId; // 그룹 상세 페이지로 리디렉션
    }
}


