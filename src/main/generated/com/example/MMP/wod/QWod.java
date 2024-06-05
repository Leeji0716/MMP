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

    public static final QWod wod = new QWod("wod");

    public final ListPath<com.example.MMP.Comment.Comment, com.example.MMP.Comment.QComment> commentList = this.<com.example.MMP.Comment.Comment, com.example.MMP.Comment.QComment>createList("commentList", com.example.MMP.Comment.Comment.class, com.example.MMP.Comment.QComment.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> createDate = createDateTime("createDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imagePath = createString("imagePath");

    public QWod(String variable) {
        super(Wod.class, forVariable(variable));
    }

    public QWod(Path<? extends Wod> path) {
        super(path.getType(), path.getMetadata());
    }

    public QWod(PathMetadata metadata) {
        super(Wod.class, metadata);
    }

}

