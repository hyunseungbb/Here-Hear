package com.ssafy.herehear.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.herehear.api.request.CommentPostReq;
import com.ssafy.herehear.api.response.BaseResponseBody;
import com.ssafy.herehear.api.service.CommentService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "감상평 API", tags = {"Comment"})
@RestController
@RequestMapping("/api/v1/comment")
public class CommentController {
	
	@Autowired
	CommentService commentService;
	
	@PostMapping("/{bookId}")
	@ApiOperation(value = "감상평 등록")
	public ResponseEntity<BaseResponseBody> createComment(@PathVariable(name = "bookId") Long bookId, 
			@RequestBody CommentPostReq req) {
		commentService.createComment(bookId, req);
		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
	}
}
