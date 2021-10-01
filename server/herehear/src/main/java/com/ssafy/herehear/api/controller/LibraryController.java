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

@Api(value = "서재 API", tags = {"Library"})
@RestController 
@RequestMapping("/api/v1/libraries")
public class LibraryController {
	
	@Autowired
	LibraryService libraryService;
	
	@Autowired
	LibraryRepository libraryRepository;
	
	@GetMapping("/mine")
	@ApiOperation(value = "���� å ��ȸ")
	public ResponseEntity<List<LibraryGetRes>> getLibrary(@ApiIgnore Authentication authentication) {
		Long userId = Long.parseLong(authentication.getName());
		System.out.println(userId);
		List<LibraryGetRes> libraryList = libraryService.getLibrary(userId);		
		return ResponseEntity.status(200).body(libraryList);
	}
	
	@PostMapping("/{bookId}")
	@ApiOperation(value = "���� å ���")
	public ResponseEntity<?> createLibrary(@PathVariable(name = "bookId") Long bookId, @ApiIgnore Authentication authentication) {
		List<Library> list = libraryRepository.findAll();
		Long userId = Long.parseLong(authentication.getName());
		// ������ ��ϵ� å�̸� POST �Ұ�
		for(Library lib : list) {
			if(lib.getAccount().getId()==userId && lib.getBook().getId()==bookId) {
				return ResponseEntity.status(412).body(BaseResponseBody.of(412, "�̹� ��ϵ� å �Դϴ�."));
			}
		}
		libraryService.createLibrary(userId, bookId);
		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
	}
	
	@PutMapping()
	@ApiOperation(value = "���� �� ���� ���� ����")
	public ResponseEntity<?> updateLibrary(@RequestBody LibraryPutReq libraryPutReq) {
		libraryService.updateLibrary(libraryPutReq);
		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
	}
	
	@DeleteMapping("/{libraryId}")
	@ApiOperation(value = "�� ���翡�� å ����")
	public ResponseEntity<?> deleteLibrary(@PathVariable Long libraryId) {
		libraryService.deleteLibrary(libraryId);
		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
	}

}
