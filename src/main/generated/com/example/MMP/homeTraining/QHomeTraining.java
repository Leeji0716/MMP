package com.example.MMP.homeTraining;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QHomeTraining is a Querydsl query type for HomeTraining
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHomeTraining extends EntityPathBase<HomeTraining> {

    private static final long serialVersionUID = -57321985L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QHomeTraining homeTraining = new QHomeTraining("homeTraining");

    public final com.example.MMP.homeTraining.category.QCategory category;

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> createDate = createDateTime("createDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<com.example.MMP.siteuser.SiteUser, com.example.MMP.siteuser.QSiteUser> saver = this.<com.example.MMP.siteuser.SiteUser, com.example.MMP.siteuser.QSiteUser>createList("saver", com.example.MMP.siteuser.SiteUser.class, com.example.MMP.siteuser.QSiteUser.class, PathInits.DIRECT2);

    public final StringPath thumbnailUrl = createString("thumbnailUrl");

    public final StringPath videoUrl = createString("videoUrl");

    public final com.example.MMP.siteuser.QSiteUser writer;

    public QHomeTraining(String variable) {
        this(HomeTraining.class, forVariable(variable), INITS);
    }

    public QHomeTraining(Path<? extends HomeTraining> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QHomeTraining(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QHomeTraining(PathMetadata metadata, PathInits inits) {
        this(HomeTraining.class, metadata, inits);
    }

    public QHomeTraining(Class<? extends HomeTraining> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.category = inits.isInitialized("category") ? new com.example.MMP.homeTraining.category.QCategory(forProperty("category")) : null;
        this.writer = inits.isInitialized("writer") ? new com.example.MMP.siteuser.QSiteUser(forProperty("writer"), inits.get("writer")) : null;
    }

}

