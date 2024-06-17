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

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSiteUser siteUser = new QSiteUser("siteUser");

    public final ListPath<com.example.MMP.alarm.Alarm, com.example.MMP.alarm.QAlarm> alarmList = this.<com.example.MMP.alarm.Alarm, com.example.MMP.alarm.QAlarm>createList("alarmList", com.example.MMP.alarm.Alarm.class, com.example.MMP.alarm.QAlarm.class, PathInits.DIRECT2);

    public final ListPath<com.example.MMP.challenge.attendance.Attendance, com.example.MMP.challenge.attendance.QAttendance> attendanceList = this.<com.example.MMP.challenge.attendance.Attendance, com.example.MMP.challenge.attendance.QAttendance>createList("attendanceList", com.example.MMP.challenge.attendance.Attendance.class, com.example.MMP.challenge.attendance.QAttendance.class, PathInits.DIRECT2);

    public final StringPath birthDate = createString("birthDate");

    public final ListPath<com.example.MMP.chat.ChatRoom, com.example.MMP.chat.QChatRoom> chatRoomList = this.<com.example.MMP.chat.ChatRoom, com.example.MMP.chat.QChatRoom>createList("chatRoomList", com.example.MMP.chat.ChatRoom.class, com.example.MMP.chat.QChatRoom.class, PathInits.DIRECT2);

    public final DatePath<java.time.LocalDate> createDate = createDate("createDate", java.time.LocalDate.class);

    public final StringPath email = createString("email");

    public final StringPath gender = createString("gender");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<com.example.MMP.lesson.Lesson, com.example.MMP.lesson.QLesson> lessonList = this.<com.example.MMP.lesson.Lesson, com.example.MMP.lesson.QLesson>createList("lessonList", com.example.MMP.lesson.Lesson.class, com.example.MMP.lesson.QLesson.class, PathInits.DIRECT2);

    public final ListPath<com.example.MMP.lesson.Lesson, com.example.MMP.lesson.QLesson> lessonsAttending = this.<com.example.MMP.lesson.Lesson, com.example.MMP.lesson.QLesson>createList("lessonsAttending", com.example.MMP.lesson.Lesson.class, com.example.MMP.lesson.QLesson.class, PathInits.DIRECT2);

    public final StringPath macAddress = createString("macAddress");

    public final StringPath name = createString("name");

    public final StringPath number = createString("number");

    public final StringPath password = createString("password");

    public final com.example.MMP.point.QPoint point;

    public final com.example.MMP.ptGroup.QPtGroup ptGroupTrainer;

    public final com.example.MMP.ptGroup.QPtGroup ptGroupUser;

    public final ListPath<com.example.MMP.homeTraining.HomeTraining, com.example.MMP.homeTraining.QHomeTraining> saveTraining = this.<com.example.MMP.homeTraining.HomeTraining, com.example.MMP.homeTraining.QHomeTraining>createList("saveTraining", com.example.MMP.homeTraining.HomeTraining.class, com.example.MMP.homeTraining.QHomeTraining.class, PathInits.DIRECT2);

    public final ListPath<com.example.MMP.transPass.TransPass, com.example.MMP.transPass.QTransPass> transPassList = this.<com.example.MMP.transPass.TransPass, com.example.MMP.transPass.QTransPass>createList("transPassList", com.example.MMP.transPass.TransPass.class, com.example.MMP.transPass.QTransPass.class, PathInits.DIRECT2);

    public final ListPath<com.example.MMP.userPass.UserDayPass, com.example.MMP.userPass.QUserDayPass> userDayPassList = this.<com.example.MMP.userPass.UserDayPass, com.example.MMP.userPass.QUserDayPass>createList("userDayPassList", com.example.MMP.userPass.UserDayPass.class, com.example.MMP.userPass.QUserDayPass.class, PathInits.DIRECT2);

    public final StringPath userId = createString("userId");

    public final ListPath<com.example.MMP.userPass.UserPtPass, com.example.MMP.userPass.QUserPtPass> userPtPassList = this.<com.example.MMP.userPass.UserPtPass, com.example.MMP.userPass.QUserPtPass>createList("userPtPassList", com.example.MMP.userPass.UserPtPass.class, com.example.MMP.userPass.QUserPtPass.class, PathInits.DIRECT2);

    public final StringPath userRole = createString("userRole");

    public QSiteUser(String variable) {
        this(SiteUser.class, forVariable(variable), INITS);
    }

    public QSiteUser(Path<? extends SiteUser> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSiteUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSiteUser(PathMetadata metadata, PathInits inits) {
        this(SiteUser.class, metadata, inits);
    }

    public QSiteUser(Class<? extends SiteUser> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.point = inits.isInitialized("point") ? new com.example.MMP.point.QPoint(forProperty("point"), inits.get("point")) : null;
        this.ptGroupTrainer = inits.isInitialized("ptGroupTrainer") ? new com.example.MMP.ptGroup.QPtGroup(forProperty("ptGroupTrainer"), inits.get("ptGroupTrainer")) : null;
        this.ptGroupUser = inits.isInitialized("ptGroupUser") ? new com.example.MMP.ptGroup.QPtGroup(forProperty("ptGroupUser"), inits.get("ptGroupUser")) : null;
    }

}

