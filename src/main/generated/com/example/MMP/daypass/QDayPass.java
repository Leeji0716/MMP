package com.example.MMP.daypass;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QDayPass is a Querydsl query type for DayPass
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDayPass extends EntityPathBase<DayPass> {

    private static final long serialVersionUID = -1862569539L;

    public static final QDayPass dayPass = new QDayPass("dayPass");

    public final NumberPath<Integer> passDays = createNumber("passDays", Integer.class);

    public final NumberPath<Long> passId = createNumber("passId", Long.class);

    public final StringPath passName = createString("passName");

    public final NumberPath<Integer> passPrice = createNumber("passPrice", Integer.class);

    public final StringPath passTitle = createString("passTitle");

    public QDayPass(String variable) {
        super(DayPass.class, forVariable(variable));
    }

    public QDayPass(Path<? extends DayPass> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDayPass(PathMetadata metadata) {
        super(DayPass.class, metadata);
    }

}

