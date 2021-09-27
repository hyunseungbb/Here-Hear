package com.ssafy.herehear.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ssafy.herehear.api.request.AccountRegisterPostReq;
import com.ssafy.herehear.db.entity.Account;
import com.ssafy.herehear.db.repository.AccountRepository;

@Service("accountService")
public class AccountServiceImpl implements AccountService {
	
	@Autowired
	AccountRepository accountRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
		
	@Override
	public Account createAccount(AccountRegisterPostReq registerInfo) {
		Account account = new Account();
		account.setUsername(registerInfo.getUsername());
		// 보안을 위해서 유저 패스워드 암호화 하여 디비에 저장.
		account.setPassword(passwordEncoder.encode(registerInfo.getPassword()));
		
		// dsl 사용
		return accountRepository.save(account);
	}
	
	@Override
	public Account getAccount(String username) {
		// DB에 아이디를 바탕으로 유저 정보 조회
		Account account = accountRepository.findByUsername(username).get();
		return account;
	}
	
	@Override
	public Account findByAccountId(Long id) {
		Account account = accountRepository.findById(id).get();
		return account;
	}
}
