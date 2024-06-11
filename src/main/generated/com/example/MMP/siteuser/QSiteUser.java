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

    public final ListPath<com.example.MMP.challenge.attendance.Attendance, com.example.MMP.challenge.attendance.QAttendance> attendanceList = this.<com.example.MMP.challenge.attendance.Attendance, com.example.MMP.challenge.attendance.QAttendance>createList("attendanceList", com.example.MMP.challenge.attendance.Attendance.class, com.example.MMP.challenge.attendance.QAttendance.class, PathInits.DIRECT2);

    public final StringPath birthDate = createString("birthDate");

    public final ListPath<com.example.MMP.challenge.challenge.Challenge, com.example.MMP.challenge.challenge.QChallenge> challenges = this.<com.example.MMP.challenge.challenge.Challenge, com.example.MMP.challenge.challenge.QChallenge>createList("challenges", com.example.MMP.challenge.challenge.Challenge.class, com.example.MMP.challenge.challenge.QChallenge.class, PathInits.DIRECT2);

    public final StringPath email = createString("email");

    public final StringPath gender = createString("gender");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final StringPath number = createString("number");

    public final StringPath password = createString("password");

    public final ListPath<com.example.MMP.homeTraining.HomeTraining, com.example.MMP.homeTraining.QHomeTraining> saveTraining = this.<com.example.MMP.homeTraining.HomeTraining, com.example.MMP.homeTraining.QHomeTraining>createList("saveTraining", com.example.MMP.homeTraining.HomeTraining.class, com.example.MMP.homeTraining.QHomeTraining.class, PathInits.DIRECT2);

    public final ListPath<com.example.MMP.userPass.UserDayPass, com.example.MMP.userPass.QUserDayPass> userDayPassList = this.<com.example.MMP.userPass.UserDayPass, com.example.MMP.userPass.QUserDayPass>createList("userDayPassList", com.example.MMP.userPass.UserDayPass.class, com.example.MMP.userPass.QUserDayPass.class, PathInits.DIRECT2);

    public final StringPath userId = createString("userId");

    public final ListPath<com.example.MMP.userPass.UserPtPass, com.example.MMP.userPass.QUserPtPass> userPtPassList = this.<com.example.MMP.userPass.UserPtPass, com.example.MMP.userPass.QUserPtPass>createList("userPtPassList", com.example.MMP.userPass.UserPtPass.class, com.example.MMP.userPass.QUserPtPass.class, PathInits.DIRECT2);

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

