package com.example.MMP.trainer;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTrainer is a Querydsl query type for Trainer
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTrainer extends EntityPathBase<Trainer> {

    private static final long serialVersionUID = -1514390611L;

    public static final QTrainer trainer = new QTrainer("trainer");

    public final StringPath birthDay = createString("birthDay");

    public final StringPath email = createString("email");

    public final StringPath gender = createString("gender");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<com.example.MMP.siteuser.SiteUser, com.example.MMP.siteuser.QSiteUser> memberList = this.<com.example.MMP.siteuser.SiteUser, com.example.MMP.siteuser.QSiteUser>createList("memberList", com.example.MMP.siteuser.SiteUser.class, com.example.MMP.siteuser.QSiteUser.class, PathInits.DIRECT2);

    public final StringPath number = createString("number");

    public final StringPath password = createString("password");

    public final StringPath trainerId = createString("trainerId");

    public final StringPath userRole = createString("userRole");

    public QTrainer(String variable) {
        super(Trainer.class, forVariable(variable));
    }

    public QTrainer(Path<? extends Trainer> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTrainer(PathMetadata metadata) {
        super(Trainer.class, metadata);
    }

}

