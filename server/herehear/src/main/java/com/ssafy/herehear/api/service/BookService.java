package com.ssafy.herehear.api.service;

import com.ssafy.herehear.db.entity.Book;

public interface BookService {
	Book findByBookId(Long book_id);
}
