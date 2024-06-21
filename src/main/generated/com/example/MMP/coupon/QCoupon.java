package com.example.MMP.coupon;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCoupon is a Querydsl query type for Coupon
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCoupon extends EntityPathBase<Coupon> {

    private static final long serialVersionUID = -1435583841L;

    public static final QCoupon coupon = new QCoupon("coupon");

    public final NumberPath<Integer> discount = createNumber("discount", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final NumberPath<Integer> point = createNumber("point", Integer.class);

    public final ListPath<com.example.MMP.siteuser.SiteUser, com.example.MMP.siteuser.QSiteUser> users = this.<com.example.MMP.siteuser.SiteUser, com.example.MMP.siteuser.QSiteUser>createList("users", com.example.MMP.siteuser.SiteUser.class, com.example.MMP.siteuser.QSiteUser.class, PathInits.DIRECT2);

    public QCoupon(String variable) {
        super(Coupon.class, forVariable(variable));
    }

    public QCoupon(Path<? extends Coupon> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCoupon(PathMetadata metadata) {
        super(Coupon.class, metadata);
    }

}

