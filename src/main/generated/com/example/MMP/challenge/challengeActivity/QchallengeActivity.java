package com.example.MMP.challenge.challengeActivity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
<<<<<<< HEAD
 * QchallengeActivity is a Querydsl query type for challengeActivity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QchallengeActivity extends EntityPathBase<challengeActivity> {

    private static final long serialVersionUID = -800045636L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QchallengeActivity challengeActivity = new QchallengeActivity("challengeActivity");

    public final DateTimePath<java.time.LocalDateTime> activeDate = createDateTime("activeDate", java.time.LocalDateTime.class);

    public final NumberPath<Integer> attendance = createNumber("attendance", Integer.class);
=======
 * QChallengeActivity is a Querydsl query type for ChallengeActivity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChallengeActivity extends EntityPathBase<ChallengeActivity> {

    private static final long serialVersionUID = -1156282980L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChallengeActivity challengeActivity = new QChallengeActivity("challengeActivity");

    public final DateTimePath<java.time.LocalDateTime> activeDate = createDateTime("activeDate", java.time.LocalDateTime.class);

    public final com.example.MMP.attendance.QAttendance attendance;
>>>>>>> 979ddd07a7d7516d4e07e05d5f3414a645de48c8

    public final com.example.MMP.challenge.challenge.QChallenge challenge;

    public final NumberPath<Integer> duration = createNumber("duration", Integer.class);

    public final NumberPath<Integer> exerciseTime = createNumber("exerciseTime", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> weight = createNumber("weight", Integer.class);

<<<<<<< HEAD
    public QchallengeActivity(String variable) {
        this(challengeActivity.class, forVariable(variable), INITS);
    }

    public QchallengeActivity(Path<? extends challengeActivity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QchallengeActivity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QchallengeActivity(PathMetadata metadata, PathInits inits) {
        this(challengeActivity.class, metadata, inits);
    }

    public QchallengeActivity(Class<? extends challengeActivity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
=======
    public QChallengeActivity(String variable) {
        this(ChallengeActivity.class, forVariable(variable), INITS);
    }

    public QChallengeActivity(Path<? extends ChallengeActivity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChallengeActivity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChallengeActivity(PathMetadata metadata, PathInits inits) {
        this(ChallengeActivity.class, metadata, inits);
    }

    public QChallengeActivity(Class<? extends ChallengeActivity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.attendance = inits.isInitialized("attendance") ? new com.example.MMP.attendance.QAttendance(forProperty("attendance"), inits.get("attendance")) : null;
>>>>>>> 979ddd07a7d7516d4e07e05d5f3414a645de48c8
        this.challenge = inits.isInitialized("challenge") ? new com.example.MMP.challenge.challenge.QChallenge(forProperty("challenge")) : null;
    }

}

