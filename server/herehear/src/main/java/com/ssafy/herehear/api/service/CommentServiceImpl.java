package com.ssafy.herehear.api.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.herehear.api.request.CommentPostReq;
import com.ssafy.herehear.db.entity.Account;
import com.ssafy.herehear.db.entity.Comment;
import com.ssafy.herehear.db.repository.AccountRepository;
import com.ssafy.herehear.db.repository.BookRepository;
import com.ssafy.herehear.db.repository.CommentRepository;

@Service("CommentService")
public class CommentServiceImpl implements CommentService {
	
	@Autowired
	CommentRepository commentRepository;
	
	@Autowired
	AccountRepository accountRepository;
	
	@Autowired
	BookRepository bookRepository;
	
	@Override
	public Comment createComment(Long bookId, CommentPostReq req) {
		Comment comment = new Comment();
		comment.setAccount(accountRepository.findById(req.getUserId()).get());
		comment.setBook(bookRepository.findById(bookId).get());
		comment.setContent(req.getContent());
		comment.setDate(new Date());
		comment.setIsshow(req.getIsshow());
		comment.setReading_time(req.getReading_time());
		
		return commentRepository.save(comment);
	}
}
