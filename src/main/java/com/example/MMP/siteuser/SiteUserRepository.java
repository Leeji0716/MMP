package com.example.MMP.siteuser;

import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface SiteUserRepository extends JpaRepository<SiteUser,Long> ,SiteUserCustom{

    Optional<SiteUser> findByUserId(String userId);
    Optional<SiteUser> findByName(String name);
    Optional<SiteUser> findByUserIdAndEmail(String userId, String email);

}
