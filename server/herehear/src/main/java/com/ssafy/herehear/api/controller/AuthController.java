package com.ssafy.herehear.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.herehear.api.request.AccountReq;
import com.ssafy.herehear.api.response.AccountRes;
import com.ssafy.herehear.api.service.AuthService;
import com.ssafy.herehear.common.jwt.TokenDto;
import com.ssafy.herehear.common.jwt.TokenReqDto;

import io.swagger.annotations.Api;

@Api(value = "회원 인증 관련 API", tags = {"Auth"})
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
	
	@Autowired
	AuthService authService;
	
	@PostMapping("/signup")
    public ResponseEntity<AccountRes> signup(@RequestBody AccountReq accountReq) {
        return ResponseEntity.ok(authService.signup(accountReq));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody AccountReq accountReq) {
        return ResponseEntity.ok(authService.login(accountReq));
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenReqDto tokenReqDto) {
        return ResponseEntity.ok(authService.reissue(tokenReqDto));
    }

}