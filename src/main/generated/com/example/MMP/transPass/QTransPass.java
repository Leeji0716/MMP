package com.example.MMP.transPass;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTransPass is a Querydsl query type for TransPass
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTransPass extends EntityPathBase<TransPass> {

    private static final long serialVersionUID = 843756725L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTransPass transPass = new QTransPass("transPass");

    public final com.example.MMP.siteuser.QSiteUser acceptUser;

    public final BooleanPath consent = createBoolean("consent");

    public final DatePath<java.time.LocalDate> createDate = createDate("createDate", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.example.MMP.siteuser.QSiteUser sendUser;

    public final com.example.MMP.userPass.QUserDayPass userDayPass;

    public final com.example.MMP.userPass.QUserPtPass userPtPass;

    public QTransPass(String variable) {
        this(TransPass.class, forVariable(variable), INITS);
    }

    public QTransPass(Path<? extends TransPass> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTransPass(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTransPass(PathMetadata metadata, PathInits inits) {
        this(TransPass.class, metadata, inits);
    }

    public QTransPass(Class<? extends TransPass> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.acceptUser = inits.isInitialized("acceptUser") ? new com.example.MMP.siteuser.QSiteUser(forProperty("acceptUser"), inits.get("acceptUser")) : null;
        this.sendUser = inits.isInitialized("sendUser") ? new com.example.MMP.siteuser.QSiteUser(forProperty("sendUser"), inits.get("sendUser")) : null;
        this.userDayPass = inits.isInitialized("userDayPass") ? new com.example.MMP.userPass.QUserDayPass(forProperty("userDayPass"), inits.get("userDayPass")) : null;
        this.userPtPass = inits.isInitialized("userPtPass") ? new com.example.MMP.userPass.QUserPtPass(forProperty("userPtPass"), inits.get("userPtPass")) : null;
    }

}

