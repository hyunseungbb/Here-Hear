package com.ssafy.herehear.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.herehear.api.request.LibraryPostReq;
import com.ssafy.herehear.api.request.LibraryPutReq;
import com.ssafy.herehear.db.entity.Account;
import com.ssafy.herehear.db.entity.Book;
import com.ssafy.herehear.db.entity.Library;
import com.ssafy.herehear.db.repository.AccountRepository;
import com.ssafy.herehear.db.repository.LibraryRepository;

@Service("LibraryService")
public class LibraryServiceImpl implements LibraryService {
	
	@Autowired
	LibraryRepository libraryRepository;
	
	@Autowired
	AccountRepository accountRepository;
	
	@Autowired
	LibraryService libraryService;
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	BookService bookService;
	
	
	@Override
	public Library findByLibraryId(Long library_id) {
		Library library = libraryRepository.findById(library_id).get();
		return library;
	}
	
	@Override
	public Library createLibrary(LibraryPostReq libraryPostReq) {
		Library library = new Library();
		Book book = bookService.findByBookId(libraryPostReq.getBook_id());
		library.setBook(book);
		
		Account account = accountService.findByAccountId(libraryPostReq.getUser_id());
		library.setAccount(account);

		library.setStars(0);
		library.setRead_status(0);
		library.setFlag(false);
		
		return libraryRepository.save(library);
	}
	
	@Override
	public Library updateLibrary(LibraryPutReq libraryPutReq) {
		Library library = libraryService.findByLibraryId(libraryPutReq.getId());
		Book book = bookService.findByBookId(library.getBook().getId());
		
		// 별점을 처음 주는 경우, flag 바꾸고 책 정보에 stars_count와 stars_sum 더해주기
		if (library.getFlag() == false) {
			library.setFlag(true);
			book.setStars_count(book.getStars_count()+1);
			book.setStars_sum(book.getStars_sum()+libraryPutReq.getStars());
		} 
		// 기존 별점이 있는 경우, 책 정보에서 stars_count에 기존 별점 빼주고 새로운 별점 더하기
		else {
			book.setStars_sum(book.getStars_sum()-library.getStars());
			book.setStars_sum(book.getStars_sum()+libraryPutReq.getStars());
		}
		// 책 정보의 stars_count와 stars_sum 수정 후, library의 값 변경
		library.setStars(libraryPutReq.getStars());
		library.setRead_status(libraryPutReq.getRead_status());
		
		return libraryRepository.save(library);
	}

}
