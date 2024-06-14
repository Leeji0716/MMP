package com.example.MMP.transPass;

import com.example.MMP.siteuser.SiteUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TransPassCustom {
    List<TransPass> MySendPass(SiteUser siteUser);
    List<TransPass> MyAcceptPass(SiteUser siteUser);

    List<TransPass> MyStandPass(SiteUser siteUser);
    Page<TransPass> AllPtStandPass(Pageable pageable);
    Page<TransPass> AllDayStandPass(Pageable pageable);
}
