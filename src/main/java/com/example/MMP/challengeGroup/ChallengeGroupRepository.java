package com.example.MMP.challengeGroup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChallengeGroupRepository extends JpaRepository<ChallengeGroup,Long> {
    @Query("SELECT g FROM ChallengeGroup g ORDER BY SIZE(g.members) DESC")
    List<ChallengeGroup> findAllOrderByMembersCountDesc();
}

