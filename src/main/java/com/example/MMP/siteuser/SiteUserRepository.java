package com.example.MMP.siteuser;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface SiteUserRepository extends JpaRepository<SiteUser,Long> ,SiteUserCustom{

    Optional<SiteUser> findByUserId(String userId);

    Optional<SiteUser> findByName(String name);

}
