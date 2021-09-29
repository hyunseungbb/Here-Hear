package com.ssafy.herehear.db.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QBook is a Querydsl query type for Book
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QBook extends EntityPathBase<Book> {

    private static final long serialVersionUID = 1600760269L;

    public static final QBook book = new QBook("book");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final StringPath author = createString("author");

    public final StringPath description = createString("description");

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath img_url = createString("img_url");

    public final StringPath isbn13 = createString("isbn13");

    public final StringPath publisher = createString("publisher");

    public final NumberPath<Integer> stars_count = createNumber("stars_count", Integer.class);

    public final NumberPath<Integer> stars_sum = createNumber("stars_sum", Integer.class);

    public final StringPath title = createString("title");

    public QBook(String variable) {
        super(Book.class, forVariable(variable));
    }

    public QBook(Path<? extends Book> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBook(PathMetadata metadata) {
        super(Book.class, metadata);
    }

}

