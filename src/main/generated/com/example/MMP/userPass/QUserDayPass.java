package com.example.MMP.userPass;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserDayPass is a Querydsl query type for UserDayPass
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserDayPass extends EntityPathBase<UserDayPass> {

    private static final long serialVersionUID = -393116097L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserDayPass userDayPass = new QUserDayPass("userDayPass");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> passFinish = createDateTime("passFinish", java.time.LocalDateTime.class);

    public final StringPath passName = createString("passName");

    public final NumberPath<Integer> passPrice = createNumber("passPrice", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> passStart = createDateTime("passStart", java.time.LocalDateTime.class);

    public final StringPath passTitle = createString("passTitle");

    public final com.example.MMP.siteuser.QSiteUser siteUser;

    public QUserDayPass(String variable) {
        this(UserDayPass.class, forVariable(variable), INITS);
    }

    public QUserDayPass(Path<? extends UserDayPass> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserDayPass(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserDayPass(PathMetadata metadata, PathInits inits) {
        this(UserDayPass.class, metadata, inits);
    }

    public QUserDayPass(Class<? extends UserDayPass> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.siteUser = inits.isInitialized("siteUser") ? new com.example.MMP.siteuser.QSiteUser(forProperty("siteUser")) : null;
    }

}

