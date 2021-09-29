package com.ssafy.herehear.api.service;

import java.util.List;

import com.ssafy.herehear.api.request.CommentPostReq;
import com.ssafy.herehear.api.response.CommentGetRes;
import com.ssafy.herehear.db.entity.Comment;

public interface CommentService {
	
	Comment createComment(Long bookId, CommentPostReq req);
	List<CommentGetRes> getAllCommentOfBook(Long bookId);
}
