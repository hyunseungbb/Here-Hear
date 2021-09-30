package com.ssafy.herehear.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.herehear.api.response.BookGetRes;
import com.ssafy.herehear.api.response.BookSearchGetRes;
import com.ssafy.herehear.api.service.BookService;
import com.ssafy.herehear.common.Search;
import com.ssafy.herehear.db.repository.BookRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "책 API", tags = {"Book"})
@RestController 
@RequestMapping("/api/v1/books")
public class BookController {
	@Autowired
	BookService bookService;
	
	@Autowired
	BookRepository bookRepository;
	
	@GetMapping("/{bookId}")
	@ApiOperation(value = "책 상세정보")
	public ResponseEntity<BookGetRes> getBook(
			@RequestBody @ApiParam(value = "책 정보", required = true) @PathVariable Long bookId) {
		
		BookGetRes bookInfo = bookService.getBook(bookId);
		
		return ResponseEntity.status(200).body(bookInfo);
	}
	
	@GetMapping("/search")
	@ApiOperation(value = "책 검색 정보")
	public ResponseEntity<List<BookSearchGetRes>> getBookBySearch(
			@RequestParam @ApiParam(value = "제목: title, 작가: author", required = true) String type, @ApiParam(value = "검색키워드", required = true) String keyword) {
		
		Search search = new Search();
		search.setType(type);
		search.setKeyword(keyword);
	
		List<BookSearchGetRes> bookList = bookService.getBookBySearch(search);
		
		return ResponseEntity.status(200).body(bookList);
	}

}
