package com.example.MMP.ptGroup;

import com.example.MMP.siteuser.SiteUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PtGroupCustomImpl implements PtGroupCustom{
    private final JPAQueryFactory jpaQueryFactory;

    QPtGroup qPtGroup = QPtGroup.ptGroup;


    public PtGroup ifUserJoined(SiteUser me, SiteUser you){

        return jpaQueryFactory.select(qPtGroup).from(qPtGroup).where(qPtGroup.trainer.eq(me).and(qPtGroup.members.contains(you))).fetchOne();
    }
}
