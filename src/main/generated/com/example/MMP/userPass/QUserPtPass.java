package com.example.MMP.userPass;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserPtPass is a Querydsl query type for UserPtPass
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserPtPass extends EntityPathBase<UserPtPass> {

    private static final long serialVersionUID = 485758339L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserPtPass userPtPass = new QUserPtPass("userPtPass");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> passCount = createNumber("passCount", Integer.class);

    public final DatePath<java.time.LocalDate> passFinish = createDate("passFinish", java.time.LocalDate.class);

    public final StringPath passName = createString("passName");

    public final NumberPath<Integer> passPrice = createNumber("passPrice", Integer.class);

    public final DatePath<java.time.LocalDate> passStart = createDate("passStart", java.time.LocalDate.class);

    public final StringPath passTitle = createString("passTitle");

    public final com.example.MMP.siteuser.QSiteUser siteUser;

    public QUserPtPass(String variable) {
        this(UserPtPass.class, forVariable(variable), INITS);
    }

    public QUserPtPass(Path<? extends UserPtPass> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserPtPass(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserPtPass(PathMetadata metadata, PathInits inits) {
        this(UserPtPass.class, metadata, inits);
    }

    public QUserPtPass(Class<? extends UserPtPass> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.siteUser = inits.isInitialized("siteUser") ? new com.example.MMP.siteuser.QSiteUser(forProperty("siteUser"), inits.get("siteUser")) : null;
    }

}

