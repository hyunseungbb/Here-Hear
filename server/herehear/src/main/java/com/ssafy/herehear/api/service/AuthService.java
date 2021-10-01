package com.ssafy.herehear.api.service;

import com.ssafy.herehear.api.request.AccountReq;
import com.ssafy.herehear.api.response.AccountRes;
import com.ssafy.herehear.common.jwt.TokenDto;
import com.ssafy.herehear.common.jwt.TokenReqDto;

/**
 *	인증 관련 비즈니스 로직 처리를 위한 서비스 인터페이스 정의.
 */
public interface AuthService {
	
	AccountRes signup(AccountReq accountReq);
	TokenDto login(AccountReq accountReq);
	TokenDto reissue(TokenReqDto toeknReqDto);
}
