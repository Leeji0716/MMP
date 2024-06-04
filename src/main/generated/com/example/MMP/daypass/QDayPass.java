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

    public final NumberPath<Integer> dayPassDays = createNumber("dayPassDays", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> dayPassFinish = createDateTime("dayPassFinish", java.time.LocalDateTime.class);

    public final NumberPath<Long> dayPassId = createNumber("dayPassId", Long.class);

    public final StringPath dayPassName = createString("dayPassName");

    public final NumberPath<Integer> dayPassPrice = createNumber("dayPassPrice", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> dayPassStart = createDateTime("dayPassStart", java.time.LocalDateTime.class);

    public final StringPath dayPassTitle = createString("dayPassTitle");

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

