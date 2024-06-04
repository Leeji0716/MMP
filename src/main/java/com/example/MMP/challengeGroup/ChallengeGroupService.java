package com.example.MMP.challengeGroup;

import com.example.MMP.siteuser.SiteUser;
import com.example.MMP.siteuser.SiteUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ChallengeGroupService {
    private final ChallengeGroupRepository challengeGroupRepository;
    private final SiteUserRepository siteUserRepository;

    public ChallengeGroup createGroup(String name, Principal principal) {
        String userId = principal.getName ();
        SiteUser leader = siteUserRepository.findByUserId (userId)
                .orElseThrow(() -> new IllegalArgumentException("찾을 수 없는 유저입니다."));

        ChallengeGroup group = new ChallengeGroup();
        group.setName(name);
        group.setLeader(leader);
        group.setCreateDate(LocalDateTime.now());

        // 그룹에 리더를 멤버로 추가
        group.getMembers().add(leader);

        return challengeGroupRepository.save(group);
    }
}
