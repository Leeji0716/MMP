package com.example.MMP.usercoupon;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserCoupon is a Querydsl query type for UserCoupon
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserCoupon extends EntityPathBase<UserCoupon> {

    private static final long serialVersionUID = -1797112609L;

    public static final QUserCoupon userCoupon = new QUserCoupon("userCoupon");

    public final NumberPath<Integer> discount = createNumber("discount", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final NumberPath<Integer> point = createNumber("point", Integer.class);

    public QUserCoupon(String variable) {
        super(UserCoupon.class, forVariable(variable));
    }

    public QUserCoupon(Path<? extends UserCoupon> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserCoupon(PathMetadata metadata) {
        super(UserCoupon.class, metadata);
    }

}

