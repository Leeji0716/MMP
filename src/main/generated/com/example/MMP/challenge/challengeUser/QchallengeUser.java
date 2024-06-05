package com.example.MMP.challenge.challengeUser;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QchallengeUser is a Querydsl query type for challengeUser
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QchallengeUser extends EntityPathBase<challengeUser> {

    private static final long serialVersionUID = 465004852L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QchallengeUser challengeUser = new QchallengeUser("challengeUser");

    public final NumberPath<Double> achievementRate = createNumber("achievementRate", Double.class);

    public final com.example.MMP.challenge.challenge.QChallenge challenge;

    public final DateTimePath<java.time.LocalDateTime> endDate = createDateTime("endDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.example.MMP.siteuser.QSiteUser siteUser;

    public final DateTimePath<java.time.LocalDateTime> startDate = createDateTime("startDate", java.time.LocalDateTime.class);

    public final BooleanPath success = createBoolean("success");

    public QchallengeUser(String variable) {
        this(challengeUser.class, forVariable(variable), INITS);
    }

    public QchallengeUser(Path<? extends challengeUser> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QchallengeUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QchallengeUser(PathMetadata metadata, PathInits inits) {
        this(challengeUser.class, metadata, inits);
    }

    public QchallengeUser(Class<? extends challengeUser> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.challenge = inits.isInitialized("challenge") ? new com.example.MMP.challenge.challenge.QChallenge(forProperty("challenge")) : null;
        this.siteUser = inits.isInitialized("siteUser") ? new com.example.MMP.siteuser.QSiteUser(forProperty("siteUser")) : null;
    }

}

