package com.example.MMP.challenge.userWeight;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserWeight is a Querydsl query type for UserWeight
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserWeight extends EntityPathBase<UserWeight> {

    private static final long serialVersionUID = -2000798476L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserWeight userWeight = new QUserWeight("userWeight");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> recordedAt = createDateTime("recordedAt", java.time.LocalDateTime.class);

    public final com.example.MMP.siteuser.QSiteUser siteUser;

    public final NumberPath<Double> weight = createNumber("weight", Double.class);

    public QUserWeight(String variable) {
        this(UserWeight.class, forVariable(variable), INITS);
    }

    public QUserWeight(Path<? extends UserWeight> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserWeight(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserWeight(PathMetadata metadata, PathInits inits) {
        this(UserWeight.class, metadata, inits);
    }

    public QUserWeight(Class<? extends UserWeight> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.siteUser = inits.isInitialized("siteUser") ? new com.example.MMP.siteuser.QSiteUser(forProperty("siteUser")) : null;
    }

}

