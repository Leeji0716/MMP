package com.example.MMP.challengeGroup;

import com.example.MMP.chat.ChatRoom;
import com.example.MMP.security.UserDetail;
import com.example.MMP.siteuser.SiteUser;
import com.example.MMP.siteuser.SiteUserRepository;
import com.example.MMP.siteuser.SiteUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import java.util.Map;

import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class ChallengeGroupService {
    private final ChallengeGroupRepository groupRepository;
    private final SiteUserRepository userRepository;
    private final SiteUserService userService;


    // GroupService에 추가
    public boolean isLeader(Long groupId, String username) {
        ChallengeGroup group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("그룹을 찾을 수 없습니다."));

        String userName = group.getLeader ().getUserId ();

        return group.getLeader ().getUserId ().equals (username);
    }

    public ChallengeGroup createGroup(String name, Principal principal, ChatRoom chatRoom) {
        String username = principal.getName();
        Optional<SiteUser> siteUsers = userRepository.findByUserId (username);

        SiteUser leader = siteUsers.get ();

        ChallengeGroup group = new ChallengeGroup();
        group.setName(name);
        group.setLeader(leader);
        group.setCreateDate(LocalDateTime.now());

        // 그룹에 리더를 멤버로 추가
        group.getMembers().add(leader);

        group.setChatRoom(chatRoom);

        return groupRepository.save(group);
    }


    public void addGroup(Long groupId, Long userId){
        Optional<ChallengeGroup> groupOpt = groupRepository.findById(groupId);
        Optional<SiteUser> userOpt = userRepository.findById(userId);

        if(groupOpt.isPresent()&&userOpt.isPresent()){
            ChallengeGroup group = groupOpt.get();
            SiteUser user = userOpt.get();
            group.getMembers().add(user);
            groupRepository.save(group);
        } else{
            throw new IllegalArgumentException("그룹이나 유저를 찾을 수 없습니다.");
        }
    }

    public void removeGroup(Long groupId, Long userId) {
        Optional<ChallengeGroup> groupOpt = groupRepository.findById(groupId);
        Optional<SiteUser> userOpt = userRepository.findById(userId);

        if (groupOpt.isPresent() && userOpt.isPresent()) {
            ChallengeGroup group = groupOpt.get();
            SiteUser user = userOpt.get();
            group.getMembers().remove(user);
            groupRepository.save(group);
        } else {
            throw new IllegalArgumentException("그룹이나 유저를 찾을 수 없습니다.");
        }
    }

    // 그룹 업데이트 메소드 추가
    public ChallengeGroup updateGroup(Long groupId, String name, String goal) {
        ChallengeGroup group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("그룹을 찾을 수 없습니다."));
        group.setName(name);
        group.setGoal(goal);
        return groupRepository.save(group);
    }

    public ChallengeGroup getGroup(Long id) {
        return groupRepository.findById(id).orElseThrow();
    }

    public List<ChallengeGroup> getAllGroups() {
        return groupRepository.findAll();
    }

    public List<ChallengeGroup> getAllGroupsSortedByMembers() {
        return groupRepository.findAllOrderByMembersCountDesc();
    }

    public void deleteGroup(Long groupId) {
        ChallengeGroup group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("그룹을 찾을 수 없습니다."));
        groupRepository.delete(group);
    }

    // 각 그룹의 총 운동 시간을 계산
    public long calculateGroupTotalAttendanceTime(ChallengeGroup group, Map<Long, Long> memberAttendanceTimeMap) {
        return group.getMembers()
                .stream()
                .mapToLong(member -> memberAttendanceTimeMap.getOrDefault(member.getId(), 0L))
                .sum();
    }

    // 모든 그룹의 총 운동 시간을 계산하고 순위를 매김
    public Map<Long, Integer> getGroupRanks(Map<Long, Long> memberAttendanceTimeMap) {
        List<ChallengeGroup> rankedGroups = groupRepository.findAll().stream()
                .sorted((group1, group2) -> Long.compare(
                        calculateGroupTotalAttendanceTime(group2, memberAttendanceTimeMap),
                        calculateGroupTotalAttendanceTime(group1, memberAttendanceTimeMap)))
                .collect(Collectors.toList());

        Map<Long, Integer> groupRanks = new HashMap<> ();
        int currentRank = 1;
        long previousTime = -1;
        int skippedRanks = 1;

        for (int i = 0; i < rankedGroups.size(); i++) {
            ChallengeGroup group = rankedGroups.get(i);
            long totalTime = calculateGroupTotalAttendanceTime(group, memberAttendanceTimeMap);

            if (totalTime != previousTime) {
                currentRank = i + 1;
                skippedRanks = 1;
            } else {
                skippedRanks++;
            }

            groupRanks.put(group.getId(), currentRank);
            previousTime = totalTime;
        }

        return groupRanks;
    }
}

