package com.example.MMP.ptpass;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPtPass is a Querydsl query type for PtPass
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPtPass extends EntityPathBase<PtPass> {

    private static final long serialVersionUID = 598029279L;

    public static final QPtPass ptPass = new QPtPass("ptPass");

    public final NumberPath<Integer> passCount = createNumber("passCount", Integer.class);

    public final NumberPath<Integer> passDays = createNumber("passDays", Integer.class);

    public final NumberPath<Long> passId = createNumber("passId", Long.class);

    public final StringPath passName = createString("passName");

    public final NumberPath<Integer> passPrice = createNumber("passPrice", Integer.class);

    public final StringPath passTitle = createString("passTitle");

    public QPtPass(String variable) {
        super(PtPass.class, forVariable(variable));
    }

    public QPtPass(Path<? extends PtPass> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPtPass(PathMetadata metadata) {
        super(PtPass.class, metadata);
    }

}

