package com.ssafy.herehear.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.herehear.db.entity.Book;
import com.ssafy.herehear.db.repository.BookRepository;

@Service("bookService")
public class BookServiceImpl implements BookService {
	@Autowired
	BookRepository bookRepository;
	
	@Override
	public Book findByBookId(Long book_id) {
		Book book = bookRepository.findById(book_id).get();
		return book;
	}
}
