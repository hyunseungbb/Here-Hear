package com.ssafy.herehear.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.herehear.api.response.AccountRes;
import com.ssafy.herehear.api.service.AccountService;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

@Api(value = "회원 조회 API", tags = {"Account"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
public class AccountController {
	
	@Autowired
	AccountService accountService;
	
	@GetMapping("/me")
	public ResponseEntity<AccountRes> getMyAccountInfo() {
		return ResponseEntity.ok(accountService.getMyInfo());
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<AccountRes> getAccountInfo(@PathVariable String username) {
		return ResponseEntity.ok(accountService.getAccountInfo(username));
	}
}
