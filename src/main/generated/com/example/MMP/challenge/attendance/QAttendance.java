package com.example.MMP.challenge.attendance;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAttendance is a Querydsl query type for Attendance
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAttendance extends EntityPathBase<Attendance> {

    private static final long serialVersionUID = 841262388L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAttendance attendance = new QAttendance("attendance");

    public final com.example.MMP.challenge.challengeActivity.QChallengeActivity challengeActivity;

    public final com.example.MMP.challengeGroup.QChallengeGroup challengeGroup;

    public final DatePath<java.time.LocalDate> date = createDate("date", java.time.LocalDate.class);

    public final DateTimePath<java.time.LocalDateTime> endTime = createDateTime("endTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath present = createBoolean("present");

    public final com.example.MMP.siteuser.QSiteUser siteUser;

    public final DateTimePath<java.time.LocalDateTime> startTime = createDateTime("startTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> totalTime = createNumber("totalTime", Long.class);

    public QAttendance(String variable) {
        this(Attendance.class, forVariable(variable), INITS);
    }

    public QAttendance(Path<? extends Attendance> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAttendance(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAttendance(PathMetadata metadata, PathInits inits) {
        this(Attendance.class, metadata, inits);
    }

    public QAttendance(Class<? extends Attendance> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.challengeActivity = inits.isInitialized("challengeActivity") ? new com.example.MMP.challenge.challengeActivity.QChallengeActivity(forProperty("challengeActivity"), inits.get("challengeActivity")) : null;
        this.challengeGroup = inits.isInitialized("challengeGroup") ? new com.example.MMP.challengeGroup.QChallengeGroup(forProperty("challengeGroup"), inits.get("challengeGroup")) : null;
        this.siteUser = inits.isInitialized("siteUser") ? new com.example.MMP.siteuser.QSiteUser(forProperty("siteUser"), inits.get("siteUser")) : null;
    }

}

