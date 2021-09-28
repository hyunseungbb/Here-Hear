package com.ssafy.herehear.api.service;

import java.util.List;

import com.ssafy.herehear.api.request.LibraryPostReq;
import com.ssafy.herehear.api.request.LibraryPutReq;
import com.ssafy.herehear.api.response.LibraryGetRes;
import com.ssafy.herehear.db.entity.Library;

public interface LibraryService {
	List<LibraryGetRes> getLibrary(Long userId);
	Library createLibrary(LibraryPostReq libraryPostReq);
	Library updateLibrary(LibraryPutReq libraryPutReq);
	void deleteLibrary(Long id);
}
