package com.ssafy.herehear.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.herehear.api.request.AccountLoginPostReq;
import com.ssafy.herehear.api.response.AccountLoginPostRes;
import com.ssafy.herehear.api.service.AccountService;
import com.ssafy.herehear.common.util.JwtTokenUtil;
import com.ssafy.herehear.db.entity.Account;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "회원 인증 API", tags = {"Auth"})
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@PostMapping("/login")
	@ApiOperation(value = "로그인")
	public ResponseEntity<AccountLoginPostRes> login(
			@RequestBody @ApiParam(value="로그인 정보", required=true) AccountLoginPostReq loginInfo) {
		String username = loginInfo.getUsername();
		String password = loginInfo.getPassword();
		
		Account account = accountService.getAccount(username);
		
		// 로그인 요청한 유저로부터 유효한 패스워드인지 확인
		if (passwordEncoder.matches(password, account.getPassword())) {
			// 유효한 패스워드인 경우, 로그인 성공
			String jwtToken = JwtTokenUtil.getToken(username);
			return ResponseEntity.ok(AccountLoginPostRes.of(200, "Success", jwtToken));
		}
		
		// 유효하지 않은 경우, 로그인 실패
		return ResponseEntity.status(401).body(AccountLoginPostRes.of(401, "Invalid Password", null));
	}
}
