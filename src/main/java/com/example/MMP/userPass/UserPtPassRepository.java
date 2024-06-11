package com.example.MMP.userPass;

import com.example.MMP.siteuser.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserPtPassRepository extends JpaRepository<UserPtPass,Long> {

    List<UserPtPass> findBySiteUser(SiteUser siteUser);
}
