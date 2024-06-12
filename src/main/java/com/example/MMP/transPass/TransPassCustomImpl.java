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
        return jpaQueryFactory.select(qTransPass).from(qTransPass).where(qTransPass.sendUser.eq(siteUser)).fetch();
    }

    public List<TransPass> MyAcceptPass(SiteUser siteUser) {
        return jpaQueryFactory.select(qTransPass).from(qTransPass).where(qTransPass.acceptUser.eq(siteUser)).fetch();
    }

}
