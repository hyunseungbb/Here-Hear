package com.ssafy.herehear.api.service;

import com.ssafy.herehear.api.request.LibraryPostReq;
import com.ssafy.herehear.api.request.LibraryPutReq;
import com.ssafy.herehear.db.entity.Library;

public interface LibraryService {
	Library createLibrary(LibraryPostReq libraryPostReq);
	Library updateLibrary(LibraryPutReq libraryPutReq);
	Library findByLibraryId(Long id);
}
