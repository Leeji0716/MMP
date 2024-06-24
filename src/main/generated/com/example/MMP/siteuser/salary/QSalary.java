package com.example.MMP.siteuser.salary;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSalary is a Querydsl query type for Salary
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSalary extends EntityPathBase<Salary> {

    private static final long serialVersionUID = 781693121L;

    public static final QSalary salary = new QSalary("salary");

    public final NumberPath<Integer> bonus = createNumber("bonus", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> incentive = createNumber("incentive", Integer.class);

    public QSalary(String variable) {
        super(Salary.class, forVariable(variable));
    }

    public QSalary(Path<? extends Salary> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSalary(PathMetadata metadata) {
        super(Salary.class, metadata);
    }

}

