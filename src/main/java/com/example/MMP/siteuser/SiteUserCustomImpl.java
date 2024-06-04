package com.example.MMP.siteuser;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class SiteUserCustomImpl implements SiteUserCustom{

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    QSiteUser qSiteUser = QSiteUser.siteUser;

    public List<SiteUser> findByqqwe(){

        return jpaQueryFactory.select(qSiteUser).from(qSiteUser).where(qSiteUser.userId.eq("admin")).fetch();
    }
}
