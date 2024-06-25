package com.example.MMP.ptGroup;

import com.example.MMP.siteuser.SiteUser;

public interface PtGroupCustom {

    PtGroup ifUserJoined(SiteUser me, SiteUser you);
}
