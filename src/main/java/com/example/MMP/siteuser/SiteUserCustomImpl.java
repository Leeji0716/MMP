package com.example.MMP.siteuser;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class SiteUserCustomImpl implements SiteUserCustom{

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    QSiteUser qSiteUser = QSiteUser.siteUser;

}
