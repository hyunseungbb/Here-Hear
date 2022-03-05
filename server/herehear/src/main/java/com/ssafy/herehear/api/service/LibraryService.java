package com.ssafy.herehear.api.service;

import java.util.List;

import com.ssafy.herehear.api.request.LibraryPutReq;
import com.ssafy.herehear.api.response.LibraryGetRes;
import com.ssafy.herehear.db.entity.Library;

public interface LibraryService {
	List<LibraryGetRes> getLibrary(Long userId);
	LibraryGetRes getLibraryDetail(Long libraryId, Long userId);
	Library createLibrary(Long userId, Long bookId);
	Library updateLibrary(LibraryPutReq libraryPutReq);
	void deleteLibrary(Long id);
}
