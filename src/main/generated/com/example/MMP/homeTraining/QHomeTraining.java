package com.example.MMP.homeTraining;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QHomeTraining is a Querydsl query type for HomeTraining
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHomeTraining extends EntityPathBase<HomeTraining> {

    private static final long serialVersionUID = -57321985L;

    public static final QHomeTraining homeTraining = new QHomeTraining("homeTraining");

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> createDate = createDateTime("createDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath videoUrl = createString("videoUrl");

    public QHomeTraining(String variable) {
        super(HomeTraining.class, forVariable(variable));
    }

    public QHomeTraining(Path<? extends HomeTraining> path) {
        super(path.getType(), path.getMetadata());
    }

    public QHomeTraining(PathMetadata metadata) {
        super(HomeTraining.class, metadata);
    }

}

