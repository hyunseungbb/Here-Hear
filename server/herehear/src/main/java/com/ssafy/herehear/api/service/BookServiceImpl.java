package com.ssafy.herehear.api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.herehear.api.response.BookGetRes;
import com.ssafy.herehear.api.response.BookSearchGetRes;
import com.ssafy.herehear.common.Search;
import com.ssafy.herehear.db.entity.Book;
import com.ssafy.herehear.db.repository.BookRepository;

@Service("bookService")
public class BookServiceImpl implements BookService {
	@Autowired
	BookRepository bookRepository;
	
	@Override
	public BookGetRes getBook(Long id) {
		Book book = bookRepository.findById(id).get();
		BookGetRes bookinfo;
		bookinfo = new BookGetRes();
		bookinfo.setId(id);
		bookinfo.setTitle(book.getTitle());
		bookinfo.setImg_url(book.getImg_url());
		bookinfo.setDescription(book.getDescription());
		bookinfo.setStars_sum(book.getStars_sum());
		bookinfo.setStars_count(book.getStars_count());

		return bookinfo;
	}
	
	@Override
	public List<BookSearchGetRes> getBookBySearch(Search search) {
		List<BookSearchGetRes> bookList = new ArrayList<>();
		List<BookSearchGetRes> bookLikeList = new ArrayList<>();
		List<Book> list = new ArrayList<>();
		
		String type = search.getType();
		
		// 검색 타입에 따라 제목/작가명이 포함된 책 리스트 추출
		if(type.equals("title")){
			list =  bookRepository.findByTitleLike("%"+search.getKeyword()+"%");
		}else if(type.equals("author")){
			list = bookRepository.findByAuthorLike("%"+search.getKeyword()+"%");
		}
		// GetRes 형식에 맞게 지정
		BookSearchGetRes res;
		for(Book b : list){
			res = new BookSearchGetRes();
			res.setId(b.getId());
			res.setTitle(b.getTitle());
			res.setImg_url(b.getImg_url());

			// 검색어와 완전히 동일하면 bookList / 아니면 bookLikeList에 담기
			if(type.equals("title")) {
				if(b.getTitle().equals(search.getKeyword())){	
					bookList.add(res);
				}else{					
					bookLikeList.add(res);
				}
			}else{
				if(b.getAuthor().equals(search.getKeyword())){
					bookList.add(res);
				}else{
					bookLikeList.add(res);
				}
			} 		
		}
		// BookLikeList를 순회하며 bookList에 다시 담아주기 ( 검색어와 동일한 책을 첫번째로 나오게 하기 위함)
		for(BookSearchGetRes b : bookLikeList){
			bookList.add(b);
		}
		
		return bookList;
	}

}
