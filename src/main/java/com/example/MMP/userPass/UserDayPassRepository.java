package com.example.MMP.userPass;

import com.example.MMP.daypass.DayPass;
import com.example.MMP.siteuser.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDayPassRepository extends JpaRepository<UserDayPass,Long> {

    List<UserDayPass> findBySiteUser(SiteUser siteUser);
}
