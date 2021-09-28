package com.ssafy.herehear.api.service;

import com.ssafy.herehear.api.request.CommentPostReq;
import com.ssafy.herehear.db.entity.Comment;

public interface CommentService {
	
	Comment createComment(Long bookId, CommentPostReq req);
}
