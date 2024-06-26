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
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public void removeGroup(Long groupId, Long userId) {
        Optional<ChallengeGroup> groupOpt = groupRepository.findById (groupId);
        Optional<SiteUser> userOpt = userRepository.findById (userId);

        if (groupOpt.isPresent () && userOpt.isPresent ()) {
            ChallengeGroup group = groupOpt.get ();
            SiteUser user = userOpt.get ();
            group.getMembers ().remove (user);
            user.getChallengeGroups ().remove (group);

            groupRepository.save (group);
            userRepository.save (user);


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
        Map<ChallengeGroup, Long> groupTotalTimes = new HashMap<>();

        for (ChallengeGroup group : challengeGroups) {
            long groupTotalTime = 0;
            for (SiteUser siteUser : group.getMembers()) {
                List<Attendance> attendances = attendanceRepository.findBySiteUserId(siteUser.getId());
                for (Attendance attendance : attendances) {
                    groupTotalTime += attendance.getTotalTime();
                }
            }
            groupTotalTimes.put(group, groupTotalTime);
        }

        // 그룹을 총 운동 시간 기준으로 내림차순 정렬
        List<ChallengeGroup> sortedGroups = new ArrayList<>(challengeGroups);
        sortedGroups.sort((g1, g2) -> Long.compare(groupTotalTimes.get(g2), groupTotalTimes.get(g1)));

        // 순위 매기기
        int rank = 1;
        long previousTime = -1;
        int sameRankCount = 0; // 이전 그룹과 동일 순위를 가진 그룹의 수를 추적

        for (int i = 0; i < sortedGroups.size(); i++) {
            ChallengeGroup group = sortedGroups.get(i);
            long currentTime = groupTotalTimes.get(group);

            if (currentTime != previousTime) { // 이전 그룹과 총 운동 시간이 다른 경우
                rank += sameRankCount; // 동일 순위 그룹 수만큼 순위 증가
                group.setRank(rank); // 현재 그룹에 새 순위 부여
                previousTime = currentTime; // 이전 시간 업데이트
                sameRankCount = 1; // 동일 순위 그룹 수 재설정
            } else { // 이전 그룹과 총 운동 시간이 같은 경우
                group.setRank(rank); // 이전 그룹과 동일한 순위 부여
                sameRankCount++; // 동일 순위 그룹 수 증가
            }
        }

        if (sameRankCount > 1) { // 마지막 그룹이 동일 순위를 가진 경우
            rank += sameRankCount - 1;
        }

        return sortedGroups; // 순위 매긴 그룹 리스트 반환
    }

    public void deleteGroup(Long groupId) {
        ChallengeGroup group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalStateException("해당 그룹을 찾을 수 없습니다."));

        List<SiteUser> groupMembers = userRepository.findAllByChallengeGroupId(groupId);
        // 그룹에 속한 모든 멤버를 제거


        for (SiteUser member : group.getMembers()) {
            member.getChallengeGroups().remove(group);
//            userRepository.save(member); // 멤버 업데이트
        }

        group.getMembers().clear(); // 그룹의 멤버 목록 클리어

        // 그룹과 연결된 채팅방에서도 모든 사용자 제거
        ChatRoom chatRoom = group.getChatRoom();
        if (chatRoom != null) {
            chatRoom.getUserList().clear(); // 채팅방의 모든 사용자 제거
            chatRoomService.save(chatRoom); // 채팅방 저장
        }
//
//        groupRepository.save(group); // 변경 사항을 데이터베이스에 반영

        // 그룹 삭제
        groupRepository.delete(group);
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

