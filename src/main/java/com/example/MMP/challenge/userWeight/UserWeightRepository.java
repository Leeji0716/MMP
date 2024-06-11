package com.example.MMP.challenge.userWeight;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserWeightRepository extends JpaRepository<UserWeight,Long> {
    List<UserWeight> findBySiteUserId(Long siteUserId);
    Optional<UserWeight> findTopBySiteUserIdOrderByRecordedAtAsc(Long siteUserId);

}
