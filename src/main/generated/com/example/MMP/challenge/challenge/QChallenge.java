package com.example.MMP.challenge.challenge;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChallenge is a Querydsl query type for Challenge
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChallenge extends EntityPathBase<Challenge> {

    private static final long serialVersionUID = 370785278L;

    public static final QChallenge challenge = new QChallenge("challenge");

    public final ListPath<com.example.MMP.challenge.challengeActivity.challengeActivity, com.example.MMP.challenge.challengeActivity.QchallengeActivity> challengeActivities = this.<com.example.MMP.challenge.challengeActivity.challengeActivity, com.example.MMP.challenge.challengeActivity.QchallengeActivity>createList("challengeActivities", com.example.MMP.challenge.challengeActivity.challengeActivity.class, com.example.MMP.challenge.challengeActivity.QchallengeActivity.class, PathInits.DIRECT2);

    public final ListPath<com.example.MMP.challenge.challengeUser.challengeUser, com.example.MMP.challenge.challengeUser.QchallengeUser> challengeUsers = this.<com.example.MMP.challenge.challengeUser.challengeUser, com.example.MMP.challenge.challengeUser.QchallengeUser>createList("challengeUsers", com.example.MMP.challenge.challengeUser.challengeUser.class, com.example.MMP.challenge.challengeUser.QchallengeUser.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> closeDate = createDateTime("closeDate", java.time.LocalDateTime.class);

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final DateTimePath<java.time.LocalDateTime> openDate = createDateTime("openDate", java.time.LocalDateTime.class);

    public final NumberPath<Integer> requiredPoint = createNumber("requiredPoint", Integer.class);

    public QChallenge(String variable) {
        super(Challenge.class, forVariable(variable));
    }

    public QChallenge(Path<? extends Challenge> path) {
        super(path.getType(), path.getMetadata());
    }

    public QChallenge(PathMetadata metadata) {
        super(Challenge.class, metadata);
    }

}

