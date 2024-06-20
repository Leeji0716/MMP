package com.example.MMP.challengeGroup;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChallengeGroup is a Querydsl query type for ChallengeGroup
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChallengeGroup extends EntityPathBase<ChallengeGroup> {

    private static final long serialVersionUID = -110039585L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChallengeGroup challengeGroup = new QChallengeGroup("challengeGroup");

    public final ListPath<com.example.MMP.challenge.attendance.Attendance, com.example.MMP.challenge.attendance.QAttendance> attendances = this.<com.example.MMP.challenge.attendance.Attendance, com.example.MMP.challenge.attendance.QAttendance>createList("attendances", com.example.MMP.challenge.attendance.Attendance.class, com.example.MMP.challenge.attendance.QAttendance.class, PathInits.DIRECT2);

    public final com.example.MMP.chat.QChatRoom chatRoom;

    public final DateTimePath<java.time.LocalDateTime> createDate = createDateTime("createDate", java.time.LocalDateTime.class);

    public final StringPath goal = createString("goal");

    public final ListPath<com.example.MMP.challengeGroup.GroupTag.GroupTag, com.example.MMP.challengeGroup.GroupTag.QGroupTag> groupTagList = this.<com.example.MMP.challengeGroup.GroupTag.GroupTag, com.example.MMP.challengeGroup.GroupTag.QGroupTag>createList("groupTagList", com.example.MMP.challengeGroup.GroupTag.GroupTag.class, com.example.MMP.challengeGroup.GroupTag.QGroupTag.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.example.MMP.siteuser.QSiteUser leader;

    public final SetPath<com.example.MMP.siteuser.SiteUser, com.example.MMP.siteuser.QSiteUser> members = this.<com.example.MMP.siteuser.SiteUser, com.example.MMP.siteuser.QSiteUser>createSet("members", com.example.MMP.siteuser.SiteUser.class, com.example.MMP.siteuser.QSiteUser.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    public QChallengeGroup(String variable) {
        this(ChallengeGroup.class, forVariable(variable), INITS);
    }

    public QChallengeGroup(Path<? extends ChallengeGroup> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChallengeGroup(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChallengeGroup(PathMetadata metadata, PathInits inits) {
        this(ChallengeGroup.class, metadata, inits);
    }

    public QChallengeGroup(Class<? extends ChallengeGroup> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.chatRoom = inits.isInitialized("chatRoom") ? new com.example.MMP.chat.QChatRoom(forProperty("chatRoom")) : null;
        this.leader = inits.isInitialized("leader") ? new com.example.MMP.siteuser.QSiteUser(forProperty("leader"), inits.get("leader")) : null;
    }

}

