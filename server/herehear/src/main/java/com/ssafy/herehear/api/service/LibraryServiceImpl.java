package com.ssafy.herehear.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jdk.nashorn.internal.runtime.options.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.herehear.api.request.LibraryPutReq;
import com.ssafy.herehear.api.response.LibraryGetRes;
import com.ssafy.herehear.db.entity.Account;
import com.ssafy.herehear.db.entity.Book;
import com.ssafy.herehear.db.entity.Library;
import com.ssafy.herehear.db.repository.AccountRepository;
import com.ssafy.herehear.db.repository.BookRepository;
import com.ssafy.herehear.db.repository.LibraryRepository;

@Service("LibraryService")
public class LibraryServiceImpl implements LibraryService {
	
	@Autowired
	LibraryRepository libraryRepository;
	
	@Autowired
	AccountRepository accountRepository;
	
	@Autowired
	BookRepository bookRepository;

	
	@Override
	public List<LibraryGetRes> getLibrary(Long userId) {
		List<Library> list = libraryRepository.findAll();
		List<LibraryGetRes> copy = new ArrayList<>();
		
		LibraryGetRes res;
		for(Library q : list) {
			if(q.getAccount().getId() == userId) {
				res = new LibraryGetRes();
				res.setId(q.getId());
				res.setBook_id(q.getBook().getId());
				res.setUser_id(userId);
				res.setRead_status(q.getRead_status());
				res.setStars(q.getStars());
				res.setImg_url(q.getBook().getImg_url());
				copy.add(res);
			}
		}
		return copy;
	}

	@Override
	public LibraryGetRes getLibraryDetail(Long libraryId, Long userId) {
		Library library =   libraryRepository.findById(libraryId).get();
		LibraryGetRes ret = new LibraryGetRes();
		Book book = library.getBook();
		ret.setBook_id(book.getId());
		ret.setId(library.getId());
		ret.setImg_url(book.getImg_url());
		ret.setRead_status(library.getRead_status());
		ret.setStars(library.getStars());
		ret.setUser_id(userId);
		return ret;
	}

	@Override
	public Library createLibrary(Long userId, Long bookId) {
		Library library = new Library();
		Book book = bookRepository.findById(bookId).get();
		library.setBook(book);
		Account account = accountRepository.findById(userId).get();
		library.setAccount(account);
		library.setStars(0);
		library.setRead_status(0);
		library.setFlag(false);
		
		return libraryRepository.save(library);
	}
	
	@Override
	public Library updateLibrary(LibraryPutReq libraryPutReq) {
		Library library = libraryRepository.findById(libraryPutReq.getId()).get();
		Book book = bookRepository.findById(library.getBook().getId()).get();
		
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
	
	@Override
	public void deleteLibrary(Long id) {
		libraryRepository.deleteById(id);
	}

}
