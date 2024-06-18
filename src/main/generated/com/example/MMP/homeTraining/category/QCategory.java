package com.example.MMP.homeTraining.category;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCategory is a Querydsl query type for Category
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCategory extends EntityPathBase<Category> {

    private static final long serialVersionUID = -1397974840L;

    public static final QCategory category = new QCategory("category");

    public final ListPath<com.example.MMP.homeTraining.HomeTraining, com.example.MMP.homeTraining.QHomeTraining> homeTrainingList = this.<com.example.MMP.homeTraining.HomeTraining, com.example.MMP.homeTraining.QHomeTraining>createList("homeTrainingList", com.example.MMP.homeTraining.HomeTraining.class, com.example.MMP.homeTraining.QHomeTraining.class, PathInits.DIRECT2);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath name = createString("name");

    public QCategory(String variable) {
        super(Category.class, forVariable(variable));
    }

    public QCategory(Path<? extends Category> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCategory(PathMetadata metadata) {
        super(Category.class, metadata);
    }

}

