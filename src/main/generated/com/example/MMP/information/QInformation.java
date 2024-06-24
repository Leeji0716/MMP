package com.example.MMP.information;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QInformation is a Querydsl query type for Information
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QInformation extends EntityPathBase<Information> {

    private static final long serialVersionUID = -1189616229L;

    public static final QInformation information = new QInformation("information");

    public final StringPath address = createString("address");

    public final StringPath callNumber = createString("callNumber");

    public final StringPath companyNumber = createString("companyNumber");

    public final StringPath email = createString("email");

    public final StringPath healthName = createString("healthName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imagePath = createString("imagePath");

    public final StringPath text = createString("text");

    public QInformation(String variable) {
        super(Information.class, forVariable(variable));
    }

    public QInformation(Path<? extends Information> path) {
        super(path.getType(), path.getMetadata());
    }

    public QInformation(PathMetadata metadata) {
        super(Information.class, metadata);
    }

}

