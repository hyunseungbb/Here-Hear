package com.ssafy.herehear.api.service;

import java.util.List;

import com.ssafy.herehear.api.response.BookGetRes;
import com.ssafy.herehear.api.response.BookSearchGetRes;
import com.ssafy.herehear.common.Search;

public interface BookService {
	BookGetRes getBook(Long id);
	List<BookSearchGetRes> getBookBySearch(Search search);
}
