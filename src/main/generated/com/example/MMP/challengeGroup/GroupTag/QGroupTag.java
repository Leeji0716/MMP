package com.example.MMP.challengeGroup.GroupTag;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGroupTag is a Querydsl query type for GroupTag
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGroupTag extends EntityPathBase<GroupTag> {

    private static final long serialVersionUID = 1992807371L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QGroupTag groupTag = new QGroupTag("groupTag");

    public final com.example.MMP.challengeGroup.QChallengeGroup group;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.example.MMP.Tag.QTag tag;

    public QGroupTag(String variable) {
        this(GroupTag.class, forVariable(variable), INITS);
    }

    public QGroupTag(Path<? extends GroupTag> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QGroupTag(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QGroupTag(PathMetadata metadata, PathInits inits) {
        this(GroupTag.class, metadata, inits);
    }

    public QGroupTag(Class<? extends GroupTag> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.group = inits.isInitialized("group") ? new com.example.MMP.challengeGroup.QChallengeGroup(forProperty("group"), inits.get("group")) : null;
        this.tag = inits.isInitialized("tag") ? new com.example.MMP.Tag.QTag(forProperty("tag")) : null;
    }

}

