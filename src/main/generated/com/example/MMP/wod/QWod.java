package com.example.MMP.wod;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWod is a Querydsl query type for Wod
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWod extends EntityPathBase<Wod> {

    private static final long serialVersionUID = -1755339685L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWod wod = new QWod("wod");

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> createDate = createDateTime("createDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imagePath = createString("imagePath");

    public final NumberPath<Long> like = createNumber("like", Long.class);

    public final ListPath<com.example.MMP.siteuser.SiteUser, com.example.MMP.siteuser.QSiteUser> likeList = this.<com.example.MMP.siteuser.SiteUser, com.example.MMP.siteuser.QSiteUser>createList("likeList", com.example.MMP.siteuser.SiteUser.class, com.example.MMP.siteuser.QSiteUser.class, PathInits.DIRECT2);

    public final com.example.MMP.siteuser.QSiteUser writer;

    public QWod(String variable) {
        this(Wod.class, forVariable(variable), INITS);
    }

    public QWod(Path<? extends Wod> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWod(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWod(PathMetadata metadata, PathInits inits) {
        this(Wod.class, metadata, inits);
    }

    public QWod(Class<? extends Wod> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.writer = inits.isInitialized("writer") ? new com.example.MMP.siteuser.QSiteUser(forProperty("writer")) : null;
    }

}

