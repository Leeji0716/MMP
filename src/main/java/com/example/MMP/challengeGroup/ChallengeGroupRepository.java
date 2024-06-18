package com.example.MMP.challengeGroup;

import com.example.MMP.siteuser.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import java.util.Optional;


public interface ChallengeGroupRepository extends JpaRepository<ChallengeGroup,Long> {
    @Query("SELECT g FROM ChallengeGroup g ORDER BY SIZE(g.members) DESC")
    List<ChallengeGroup> findAllOrderByMembersCountDesc();


    List<ChallengeGroup> findByMembersContaining(SiteUser siteUser);
}

