package com.example.MMP.transPass;

import com.example.MMP.siteuser.SiteUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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

    public Page<TransPass> AllPtStandPass(Pageable pageable){


        // Fetch the total count
        long total = jpaQueryFactory.select(qTransPass.count())
                .from(qTransPass)
                .where(qTransPass.consent.eq(true).and(qTransPass.userPtPass.isNotNull()))
                .fetchOne();

        // Fetch the paginated data
        List<TransPass> transPassList = jpaQueryFactory.selectFrom(qTransPass)
                .where(qTransPass.consent.eq(true).and(qTransPass.userPtPass.isNotNull()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(transPassList, pageable, total);
    }

    public Page<TransPass> AllDayStandPass(Pageable pageable){


        // Fetch the total count
        long total = jpaQueryFactory.select(qTransPass.count())
                .from(qTransPass)
                .where(qTransPass.consent.eq(true).and(qTransPass.userDayPass.isNotNull()))
                .fetchOne();

        // Fetch the paginated data
        List<TransPass> transPassList = jpaQueryFactory.selectFrom(qTransPass)
                .where(qTransPass.consent.eq(true).and(qTransPass.userDayPass.isNotNull()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(transPassList, pageable, total);
    }
}
