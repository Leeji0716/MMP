package com.example.MMP.ptpass;

import com.example.MMP.siteuser.SiteUser;
import com.example.MMP.siteuser.SiteUserRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PtPassRepository extends JpaRepository<PtPass,Long> {
    Optional<PtPass> findByPassName(String name);


}