package com.example.MMP.challenge.challengeUser;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChallengeUser is a Querydsl query type for ChallengeUser
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChallengeUser extends EntityPathBase<ChallengeUser> {

    private static final long serialVersionUID = 1263966484L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChallengeUser challengeUser = new QChallengeUser("challengeUser");

    public final NumberPath<Double> achievementRate = createNumber("achievementRate", Double.class);

    public final com.example.MMP.challenge.challenge.QChallenge challenge;

    public final DateTimePath<java.time.LocalDateTime> endDate = createDateTime("endDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> initialExerciseTime = createNumber("initialExerciseTime", Integer.class);

    public final NumberPath<Double> initialWeight = createNumber("initialWeight", Double.class);

    public final com.example.MMP.siteuser.QSiteUser siteUser;

    public final DateTimePath<java.time.LocalDateTime> startDate = createDateTime("startDate", java.time.LocalDateTime.class);

    public final BooleanPath success = createBoolean("success");

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
        super(type, metadata, inits);
        this.challenge = inits.isInitialized("challenge") ? new com.example.MMP.challenge.challenge.QChallenge(forProperty("challenge")) : null;
        this.siteUser = inits.isInitialized("siteUser") ? new com.example.MMP.siteuser.QSiteUser(forProperty("siteUser"), inits.get("siteUser")) : null;
    }

}

