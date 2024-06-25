package com.example.MMP.siteuser;

import org.springframework.data.jpa.repository.JpaRepository;

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

}