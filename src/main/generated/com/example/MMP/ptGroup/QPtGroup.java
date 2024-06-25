package com.example.MMP.ptGroup;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPtGroup is a Querydsl query type for PtGroup
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPtGroup extends EntityPathBase<PtGroup> {

    private static final long serialVersionUID = -313282055L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPtGroup ptGroup = new QPtGroup("ptGroup");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<com.example.MMP.siteuser.SiteUser, com.example.MMP.siteuser.QSiteUser> members = this.<com.example.MMP.siteuser.SiteUser, com.example.MMP.siteuser.QSiteUser>createList("members", com.example.MMP.siteuser.SiteUser.class, com.example.MMP.siteuser.QSiteUser.class, PathInits.DIRECT2);

    public final com.example.MMP.siteuser.QSiteUser trainer;

    public QPtGroup(String variable) {
        this(PtGroup.class, forVariable(variable), INITS);
    }

    public QPtGroup(Path<? extends PtGroup> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPtGroup(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPtGroup(PathMetadata metadata, PathInits inits) {
        this(PtGroup.class, metadata, inits);
    }

    public QPtGroup(Class<? extends PtGroup> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.trainer = inits.isInitialized("trainer") ? new com.example.MMP.siteuser.QSiteUser(forProperty("trainer"), inits.get("trainer")) : null;
    }

}

