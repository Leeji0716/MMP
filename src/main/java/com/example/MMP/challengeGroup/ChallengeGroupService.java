package com.example.MMP.challengeGroup;

import com.example.MMP.siteuser.SiteUser;
import com.example.MMP.siteuser.SiteUserRepository;
import com.example.MMP.siteuser.SiteUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
        return group.getLeader().getName().equals(username);
    }

    public ChallengeGroup createGroup(String name, Principal principal) {
        String username = principal.getName();
        String nameChange = userService.getNumberByName (username);
        SiteUser leader = userRepository.findByName(nameChange)
                .orElseThrow(() -> new IllegalArgumentException("찾을 수 없는 유저입니다."));

        ChallengeGroup group = new ChallengeGroup();
        group.setName(name);
        group.setLeader(leader);
        group.setCreateDate(LocalDateTime.now());

        // 그룹에 리더를 멤버로 추가
        group.getMembers().add(leader);

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
}
