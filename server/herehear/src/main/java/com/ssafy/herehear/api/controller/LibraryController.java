package com.ssafy.herehear.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.herehear.api.request.LibraryPutReq;
import com.ssafy.herehear.api.response.BaseResponseBody;
import com.ssafy.herehear.api.response.LibraryGetRes;
import com.ssafy.herehear.api.service.LibraryService;
import com.ssafy.herehear.db.entity.Library;
import com.ssafy.herehear.db.repository.LibraryRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@Api(value = "내 서재 API", tags = {"Library"})
@RestController 
@RequestMapping("/api/v1/libraries")
public class LibraryController {
	
	@Autowired
	LibraryService libraryService;
	
	@Autowired
	LibraryRepository libraryRepository;
	
	@GetMapping("/mine")
	@ApiOperation(value = "서재 책 조회")
	public ResponseEntity<List<LibraryGetRes>> getLibrary(@ApiIgnore Authentication authentication) {
		Long userId = Long.parseLong(authentication.getName());
		System.out.println(userId);
		List<LibraryGetRes> libraryList = libraryService.getLibrary(userId);		
		return ResponseEntity.status(200).body(libraryList);
	}
	
	@PostMapping("/{bookId}")
	@ApiOperation(value = "서재 책 등록")
	public ResponseEntity<?> createLibrary(@PathVariable(name = "bookId") Long bookId, @ApiIgnore Authentication authentication) {
		List<Library> list = libraryRepository.findAll();
		Long userId = Long.parseLong(authentication.getName());
		// 기존에 등록된 책이면 POST 불가
		for(Library lib : list) {
			if(lib.getAccount().getId()==userId && lib.getBook().getId()==bookId) {
				return ResponseEntity.status(412).body(BaseResponseBody.of(412, "이미 등록된 책 입니다."));
			}
		}
		libraryService.createLibrary(userId, bookId);
		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
	}
	
	@PutMapping()
	@ApiOperation(value = "별점 및 상태 수정")
	public ResponseEntity<?> updateLibrary(@RequestBody LibraryPutReq libraryPutReq) {
		libraryService.updateLibrary(libraryPutReq);
		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
	}
	
	@DeleteMapping("/{libraryId}")
	@ApiOperation(value = "내 서재에서 책 삭제")
	public ResponseEntity<?> deleteLibrary(@PathVariable Long libraryId) {
		libraryService.deleteLibrary(libraryId);
		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
	}

}
