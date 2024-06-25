package com.example.MMP.challengeGroup;

import com.example.MMP.challenge.attendance.Attendance;
import com.example.MMP.challenge.attendance.AttendanceRepository;
import com.example.MMP.chat.ChatMessageService;
import com.example.MMP.chat.ChatRoom;
import com.example.MMP.chat.ChatRoomService;
import com.example.MMP.siteuser.SiteUser;
import com.example.MMP.siteuser.SiteUserRepository;
import com.example.MMP.siteuser.SiteUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class ChallengeGroupService {
    private final ChallengeGroupRepository groupRepository;
    private final SiteUserRepository userRepository;
    private final SiteUserService userService;
    private final ChatRoomService chatRoomService;
    private final AttendanceRepository attendanceRepository;


    // GroupService에 추가
    public boolean isLeader(Long groupId, String username) {
        ChallengeGroup group = groupRepository.findById (groupId)
                .orElseThrow (() -> new IllegalArgumentException ("그룹을 찾을 수 없습니다."));

        String userName = group.getLeader ().getUserId ();

        return group.getLeader ().getUserId ().equals (username);
    }

    public ChallengeGroup createGroup(String name, Principal principal, ChatRoom chatRoom) {
        String username = principal.getName ();
        Optional<SiteUser> siteUsers = userRepository.findByUserId (username);

        SiteUser leader = siteUsers.get ();

        ChallengeGroup group = new ChallengeGroup ();
        group.setName (name);
        group.setLeader (leader);
        group.setCreateDate (LocalDateTime.now ());

        // 그룹에 리더를 멤버로 추가
        group.getMembers ().add (leader);

        group.setChatRoom (chatRoom);

        leader.getChallengeGroups ().add (group);

        return groupRepository.save (group);
    }


    public void addGroup(Long groupId, Long userId) {
        Optional<ChallengeGroup> groupOpt = groupRepository.findById (groupId);
        Optional<SiteUser> userOpt = userRepository.findById (userId);

        if (groupOpt.isPresent () && userOpt.isPresent ()) {
            ChallengeGroup group = groupOpt.get ();
            SiteUser user = userOpt.get ();
            group.getMembers ().add (user);

            ChatRoom chatRoom = group.getChatRoom();
            chatRoom.getUserList().add(user);
            chatRoomService.save(chatRoom);
            user.getChallengeGroups().add(group);
            user.getChatRoomList().add(chatRoom);

            userService.save (user);
            groupRepository.save (group);
        } else {
            throw new IllegalArgumentException ("그룹이나 유저를 찾을 수 없습니다.");
        }
    }

    // 그룹 탈퇴 기능
    public void removeGroup(Long groupId, Long userId) {
        Optional<ChallengeGroup> groupOpt = groupRepository.findById (groupId);
        Optional<SiteUser> userOpt = userRepository.findById (userId);

        if (groupOpt.isPresent () && userOpt.isPresent ()) {
            ChallengeGroup group = groupOpt.get ();
            SiteUser user = userOpt.get ();
            group.getMembers ().remove (user);
            groupRepository.save (group);


            ChatRoom chatRoom = chatRoomService.findById(group.getChatRoom().getId());
            chatRoom.getUserList().remove(user);
            chatRoomService.save(chatRoom);

            user.getChatRoomList().remove(chatRoom);
            userService.save(user);

        } else {
            throw new IllegalArgumentException ("그룹이나 유저를 찾을 수 없습니다.");
        }
    }

    // 그룹 업데이트 메소드 추가
    public ChallengeGroup updateGroup(Long groupId, String name, String goal) {
        ChallengeGroup group = groupRepository.findById (groupId)
                .orElseThrow (() -> new IllegalArgumentException ("그룹을 찾을 수 없습니다."));
        group.setName (name);
        group.setGoal (goal);
        return groupRepository.save (group);
    }

    public ChallengeGroup getGroup(Long id) {
        return groupRepository.findById (id).orElseThrow ();
    }

    public List<ChallengeGroup> getAllGroups() {
        return groupRepository.findAll ();
    }

    public List<ChallengeGroup> getAllGroupsSortedByMembers() {
        return groupRepository.findAllOrderByMembersCountDesc ();
    }

    // 각 그룹의 총 운동 시간을 계산
    public long calculateGroupTotalAttendanceTime(ChallengeGroup group, Map<Long, Long> memberAttendanceTimeMap) {
        return group.getMembers ()
                .stream ()
                .mapToLong (member -> memberAttendanceTimeMap.getOrDefault (member.getId (), 0L))
                .sum ();
    }


    // 모든 그룹의 총 운동 시간을 계산하고 순위를 매김
    public List<ChallengeGroup> getGroupRanks() {
        List<ChallengeGroup> challengeGroups = groupRepository.findAll();

        // 그룹별 총 운동 시간을 저장할 리스트
        List<Long> groupTotalTimes = new ArrayList<>();

        // 각 그룹의 총 운동 시간을 계산하여 리스트에 저장
        for (ChallengeGroup group : challengeGroups) {
            List<Attendance> attendances = attendanceRepository.findByChallengeGroupId(group.getId());
            long groupTotalTime = 0;

            for (Attendance attendance : attendances) {
                groupTotalTime += attendance.getTotalTime();
            }
//            Long calGroupTotalTime = groupTotalTime/(group.getMembers ().size ());
            Long calGroupTotalTime = groupTotalTime;
            groupTotalTimes.add(calGroupTotalTime);
        }

        // 그룹과 총 운동 시간을 기준으로 그룹을 내림차순으로 정렬
        List<ChallengeGroup> sortedGroups = new ArrayList<>(challengeGroups);
        sortedGroups.sort((g1, g2) -> Long.compare(
                groupTotalTimes.get(challengeGroups.indexOf(g2)),
                groupTotalTimes.get(challengeGroups.indexOf(g1))
        ));

        // 동일한 토탈 시간에 동일 순위 부여
        int rank = 1;
        long previousTime = -1;  // 이전 그룹의 총 운동 시간
        int sameRankCount = 0;  // 동일한 순위를 가진 그룹의 수

        for (int i = 0; i < sortedGroups.size(); i++) {
            ChallengeGroup group = sortedGroups.get(i);
            long currentTime = groupTotalTimes.get(challengeGroups.indexOf(group));

            if (currentTime == previousTime) {
                group.setRank(rank);
                sameRankCount++;
            } else {
                rank += sameRankCount;
                sameRankCount = 1;
                group.setRank(rank);
            }
            previousTime = currentTime;
        }

        // 정렬된 그룹 리스트를 반환하거나 필요한 경우 저장
        return sortedGroups; // 순위 매긴 그룹의 수를 반환 (필요시)
    }

    public void deleteGroup(ChallengeGroup challengeGroup) {
        groupRepository.delete (challengeGroup);
    }

    public ChallengeGroup findByName(String cName) {
        return groupRepository.findByName (cName);
    }

    public ChallengeGroup changeLeader(Long groupId, Long newLeaderId) {

        ChallengeGroup group = groupRepository.findById(groupId).orElseThrow(() -> new EntityNotFoundException("그룹을 찾을 수 없습니다."));
        SiteUser newLeader = userRepository.findById(newLeaderId).orElseThrow(() -> new EntityNotFoundException ("유저를 찾을 수 없습니다."));
        group.setLeader(newLeader);

        return groupRepository.save(group);
    }
}

