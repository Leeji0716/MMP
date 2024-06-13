package com.example.MMP.transPass;

import com.example.MMP.siteuser.SiteUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TransPassCustomImpl implements TransPassCustom {
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    QTransPass qTransPass = QTransPass.transPass;

    public List<TransPass> MySendPass(SiteUser siteUser) {
        return jpaQueryFactory.select(qTransPass).from(qTransPass).where(qTransPass.sendUser.eq(siteUser).and(qTransPass.consent.eq(false))).fetch();
    }

    public List<TransPass> MyAcceptPass(SiteUser siteUser) {
        return jpaQueryFactory.select(qTransPass).from(qTransPass).where(qTransPass.acceptUser.eq(siteUser).and(qTransPass.consent.eq(false))).fetch();
    }

    public List<TransPass> MyStandPass(SiteUser siteUser) {
        return jpaQueryFactory.select(qTransPass).from(qTransPass).where(qTransPass.sendUser.eq(siteUser).or(qTransPass.acceptUser.eq(siteUser)).and(qTransPass.consent.eq(true))).fetch();
    }
}
