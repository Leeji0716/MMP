package com.example.MMP.wod;

import com.example.MMP.siteuser.SiteUser;

import java.util.List;

public interface WodCustom{
    List<Wod> findByUserWod(SiteUser siteUser);
}
