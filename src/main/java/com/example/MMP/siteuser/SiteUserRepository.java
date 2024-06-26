package com.example.MMP.siteuser;

import com.example.MMP.challengeGroup.ChallengeGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SiteUserRepository extends JpaRepository<SiteUser,Long> ,SiteUserCustom{

    Optional<SiteUser> findByUserId(String userId);
    Optional<SiteUser> findByName(String name);
    Optional<SiteUser> findByUserIdAndEmail(String userId, String email);

    Optional<SiteUser> findByNumber(String number);
    long countByReferrer(SiteUser referrer); // 추가된 메서드
    List<SiteUser> findByUserRole(String useRole);

    Optional<SiteUser> findByEmail(String email);

    // 특정 그룹에 속한 SiteUser 리스트를 반환
    @Query("SELECT su FROM SiteUser su JOIN su.challengeGroups cg WHERE cg.id = :groupId")
    List<SiteUser> findAllByChallengeGroupId(@Param("groupId") Long groupId);
}