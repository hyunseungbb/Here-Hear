package com.ssafy.herehear.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.herehear.api.request.CommentPostReq;
import com.ssafy.herehear.api.response.BaseResponseBody;
import com.ssafy.herehear.api.response.CommentGetRes;
import com.ssafy.herehear.api.service.CommentService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

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
	
	@GetMapping("/{bookId}")
	@ApiOperation(value = "전체 감상평 조회")
	public ResponseEntity<List<CommentGetRes>> getAllComment(@PathVariable(name = "bookId") Long bookId) {
		List<CommentGetRes> commentList = commentService.getAllCommentOfBook(bookId);
		return ResponseEntity.status(200).body(commentList);
	}
	
	@GetMapping("/my/{bookId}")
	@ApiOperation(value = "나의 감상평 조회")
	public ResponseEntity<List<CommentGetRes>> gellAllMyComment(@PathVariable(name = "bookId") Long bookId, @ApiIgnore Authentication authentication) {
		Long userId = Long.parseLong(authentication.getName());
		List<CommentGetRes> commentList = commentService.getAllMyCommentOfBook(bookId, userId);
		return ResponseEntity.status(200).body(commentList);
	}
}
