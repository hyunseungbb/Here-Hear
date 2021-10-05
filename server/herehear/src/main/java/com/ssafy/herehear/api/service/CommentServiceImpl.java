package com.ssafy.herehear.api.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.herehear.api.request.CommentPostReq;
import com.ssafy.herehear.api.response.CommentGetRes;
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
	public Comment createComment(Long bookId, CommentPostReq req, Long userId) {
		Comment comment = new Comment();
		comment.setAccount(accountRepository.findById(userId).get());
		comment.setBook(bookRepository.findById(bookId).get());
		comment.setContent(req.getContent());
		comment.setDate(new Date());
		comment.setIsshow(req.getIsshow());
		comment.setReading_time(req.getReading_time());
		
		return commentRepository.save(comment);
	}
	
	@Override
	public List<CommentGetRes> getAllCommentOfBook(Long bookId) {
		List<Comment> commentList = commentRepository.findAll();
		List<CommentGetRes> res = new ArrayList<>();
		
		CommentGetRes tmpRes;
		
		for(Comment comment : commentList) {
			if(comment.getBook().getId() == bookId) {
				tmpRes = new CommentGetRes();
				tmpRes.setId(comment.getId());
				tmpRes.setUsername(comment.getAccount().getUsername());
				tmpRes.setBookId(comment.getBook().getId());
				tmpRes.setContent(comment.getContent());
				tmpRes.setDate(comment.getDate());
				tmpRes.setReading_time(comment.getReading_time());
				tmpRes.setIsshow(comment.getIsshow());
				res.add(tmpRes);
			}
		}
		return res;
	}
	
	@Override
	public List<CommentGetRes> getAllMyCommentOfBook(Long bookId, Long userId) {
		List<Comment> commentList = commentRepository.findAll();
		List<CommentGetRes> res = new ArrayList<>();
		
		CommentGetRes tmpRes;
		
		for(Comment comment : commentList) {
			if(comment.getBook().getId() == bookId && comment.getAccount().getId() == userId) {
				tmpRes = new CommentGetRes();
				tmpRes.setId(comment.getId());
				tmpRes.setUsername(comment.getAccount().getUsername());
				tmpRes.setBookId(comment.getBook().getId());
				tmpRes.setContent(comment.getContent());
				tmpRes.setDate(comment.getDate());
				tmpRes.setReading_time(comment.getReading_time());
				tmpRes.setIsshow(comment.getIsshow());
				res.add(tmpRes);
			}
		}
		return res;
	}
	
	@Override
	public List<CommentGetRes> getAllMyComment(Long userId) {
		List<Comment> commentList = commentRepository.findAll();
		List<CommentGetRes> res = new ArrayList<>();
		
		CommentGetRes tmpRes;
		
		for(Comment comment : commentList) {
			if(comment.getAccount().getId() == userId) {
				tmpRes = new CommentGetRes();
				tmpRes.setId(comment.getId());
				tmpRes.setUsername(comment.getAccount().getUsername());
				tmpRes.setBookId(comment.getBook().getId());
				tmpRes.setContent(comment.getContent());
				tmpRes.setDate(comment.getDate());
				tmpRes.setReading_time(comment.getReading_time());
				tmpRes.setIsshow(comment.getIsshow());
				res.add(tmpRes);
			}
		}
		return res;
	}
}
