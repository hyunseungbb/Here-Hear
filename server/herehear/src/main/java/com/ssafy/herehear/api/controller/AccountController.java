package com.ssafy.herehear.api.controller;

import com.ssafy.herehear.api.request.AccountRegisterPostReq;
import com.ssafy.herehear.api.response.BaseResponseBody;
import com.ssafy.herehear.api.service.AccountService;
import com.ssafy.herehear.db.entity.Account;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus; 
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity; 
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.representer.BaseRepresenter;

import java.util.List; 
import java.util.Optional;

@Api(value = "회원 API", tags = {"Account"})
@RestController 
@RequestMapping("/api/v1/accounts")
public class AccountController {
	
	@Autowired
	AccountService accountService;
	
	@PostMapping()
	@ApiOperation(value = "회원 가입")
	public ResponseEntity<?> register(
			@RequestBody @ApiParam(value = "회원가입 정보", required = true) AccountRegisterPostReq registerInfo) {
		
		Account account = accountService.createAccount(registerInfo);
		
		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
	}
	
//	@GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE }) 
//	public ResponseEntity<Account> getMember(@PathVariable("id") Long id) { 
//		Optional<Account> user = accountService.findById(id); 
//		return new ResponseEntity<Account>(user.get(), HttpStatus.OK); 
//	}

}
