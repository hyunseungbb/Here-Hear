package com.ssafy.herehear.db.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBook_Tag is a Querydsl query type for Book_Tag
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QBook_Tag extends EntityPathBase<Book_Tag> {

    private static final long serialVersionUID = -605916664L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBook_Tag book_Tag = new QBook_Tag("book_Tag");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final QBook book;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final QTag tag;

    public QBook_Tag(String variable) {
        this(Book_Tag.class, forVariable(variable), INITS);
    }

    public QBook_Tag(Path<? extends Book_Tag> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBook_Tag(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBook_Tag(PathMetadata metadata, PathInits inits) {
        this(Book_Tag.class, metadata, inits);
    }

    public QBook_Tag(Class<? extends Book_Tag> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.book = inits.isInitialized("book") ? new QBook(forProperty("book")) : null;
        this.tag = inits.isInitialized("tag") ? new QTag(forProperty("tag")) : null;
    }

}

