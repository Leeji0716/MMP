package com.example.MMP.challenge.challengeUser;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
<<<<<<< HEAD
 * QchallengeUser is a Querydsl query type for challengeUser
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QchallengeUser extends EntityPathBase<challengeUser> {

    private static final long serialVersionUID = 465004852L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QchallengeUser challengeUser = new QchallengeUser("challengeUser");
=======
 * QChallengeUser is a Querydsl query type for ChallengeUser
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChallengeUser extends EntityPathBase<ChallengeUser> {

    private static final long serialVersionUID = 1263966484L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChallengeUser challengeUser = new QChallengeUser("challengeUser");
>>>>>>> 979ddd07a7d7516d4e07e05d5f3414a645de48c8

    public final NumberPath<Double> achievementRate = createNumber("achievementRate", Double.class);

    public final com.example.MMP.challenge.challenge.QChallenge challenge;

    public final DateTimePath<java.time.LocalDateTime> endDate = createDateTime("endDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.example.MMP.siteuser.QSiteUser siteUser;

    public final DateTimePath<java.time.LocalDateTime> startDate = createDateTime("startDate", java.time.LocalDateTime.class);

    public final BooleanPath success = createBoolean("success");

<<<<<<< HEAD
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
=======
    public QChallengeUser(String variable) {
        this(ChallengeUser.class, forVariable(variable), INITS);
    }

    public QChallengeUser(Path<? extends ChallengeUser> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChallengeUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChallengeUser(PathMetadata metadata, PathInits inits) {
        this(ChallengeUser.class, metadata, inits);
    }

    public QChallengeUser(Class<? extends ChallengeUser> type, PathMetadata metadata, PathInits inits) {
>>>>>>> 979ddd07a7d7516d4e07e05d5f3414a645de48c8
        super(type, metadata, inits);
        this.challenge = inits.isInitialized("challenge") ? new com.example.MMP.challenge.challenge.QChallenge(forProperty("challenge")) : null;
        this.siteUser = inits.isInitialized("siteUser") ? new com.example.MMP.siteuser.QSiteUser(forProperty("siteUser")) : null;
    }

}

