package com.ssafy.herehear.db.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QLibrary is a Querydsl query type for Library
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QLibrary extends EntityPathBase<Library> {

    private static final long serialVersionUID = 1328913239L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QLibrary library = new QLibrary("library");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final QAccount account;

    public final QBook book;

    public final BooleanPath flag = createBoolean("flag");

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final NumberPath<Integer> read_status = createNumber("read_status", Integer.class);

    public final NumberPath<Integer> stars = createNumber("stars", Integer.class);

    public QLibrary(String variable) {
        this(Library.class, forVariable(variable), INITS);
    }

    public QLibrary(Path<? extends Library> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QLibrary(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QLibrary(PathMetadata metadata, PathInits inits) {
        this(Library.class, metadata, inits);
    }

    public QLibrary(Class<? extends Library> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.account = inits.isInitialized("account") ? new QAccount(forProperty("account")) : null;
        this.book = inits.isInitialized("book") ? new QBook(forProperty("book")) : null;
    }

}

