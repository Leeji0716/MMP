package com.example.MMP.transPass;

import com.example.MMP.siteuser.SiteUser;

import java.util.List;

public interface TransPassCustom {
    List<TransPass> MySendPass(SiteUser siteUser);
    List<TransPass> MyAcceptPass(SiteUser siteUser);
}
