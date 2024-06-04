package com.example.MMP.siteuser;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteUserRepository extends JpaRepository<SiteUser,Long> ,SiteUserCustom{

}
