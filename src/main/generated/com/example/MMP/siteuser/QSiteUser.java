package com.example.MMP.siteuser;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSiteUser is a Querydsl query type for SiteUser
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSiteUser extends EntityPathBase<SiteUser> {

    private static final long serialVersionUID = 1960345023L;

    public static final QSiteUser siteUser = new QSiteUser("siteUser");

    public final StringPath birthDate = createString("birthDate");

    public final ListPath<com.example.MMP.daypass.DayPass, com.example.MMP.daypass.QDayPass> dayPassList = this.<com.example.MMP.daypass.DayPass, com.example.MMP.daypass.QDayPass>createList("dayPassList", com.example.MMP.daypass.DayPass.class, com.example.MMP.daypass.QDayPass.class, PathInits.DIRECT2);

    public final StringPath email = createString("email");

    public final StringPath gender = createString("gender");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final StringPath number = createString("number");

    public final StringPath password = createString("password");

    public final ListPath<com.example.MMP.ptpass.PtPass, com.example.MMP.ptpass.QPtPass> ptPassList = this.<com.example.MMP.ptpass.PtPass, com.example.MMP.ptpass.QPtPass>createList("ptPassList", com.example.MMP.ptpass.PtPass.class, com.example.MMP.ptpass.QPtPass.class, PathInits.DIRECT2);

    public final ListPath<com.example.MMP.trainer.Trainer, com.example.MMP.trainer.QTrainer> trainerList = this.<com.example.MMP.trainer.Trainer, com.example.MMP.trainer.QTrainer>createList("trainerList", com.example.MMP.trainer.Trainer.class, com.example.MMP.trainer.QTrainer.class, PathInits.DIRECT2);

    public final StringPath userId = createString("userId");

    public final StringPath userRole = createString("userRole");

    public final ListPath<com.example.MMP.wod.Wod, com.example.MMP.wod.QWod> wodList = this.<com.example.MMP.wod.Wod, com.example.MMP.wod.QWod>createList("wodList", com.example.MMP.wod.Wod.class, com.example.MMP.wod.QWod.class, PathInits.DIRECT2);

    public QSiteUser(String variable) {
        super(SiteUser.class, forVariable(variable));
    }

    public QSiteUser(Path<? extends SiteUser> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSiteUser(PathMetadata metadata) {
        super(SiteUser.class, metadata);
    }

}

