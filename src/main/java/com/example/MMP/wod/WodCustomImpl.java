package com.example.MMP.wod;

import com.example.MMP.siteuser.SiteUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
@RequiredArgsConstructor
public class WodCustomImpl implements WodCustom{
    private final JPAQueryFactory jpaQueryFactory;

    QWod qWod = QWod.wod;
    @Override
    public List<Wod> findByUserWod(SiteUser siteUser) {
        return jpaQueryFactory.select(qWod).from(qWod).where(qWod.writer.eq(siteUser)).fetch();
    }
}
