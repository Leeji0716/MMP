package com.example.MMP.challenge.challengeActivity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChallengeActivityRepository extends JpaRepository<ChallengeActivity,Long> {
    List<ChallengeActivity> findByChallengeId(Long challengeId);
}
