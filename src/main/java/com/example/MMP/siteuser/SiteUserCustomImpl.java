package com.example.MMP.siteuser;

import com.example.MMP.transPass.TransPass;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class SiteUserCustomImpl implements SiteUserCustom{

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    QSiteUser qSiteUser = QSiteUser.siteUser;

}
