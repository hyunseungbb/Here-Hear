package com.ssafy.herehear.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.herehear.api.response.AccountRes;
import com.ssafy.herehear.common.util.SecurityUtil;
import com.ssafy.herehear.db.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

@Service("accountService")
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
	
	@Autowired
	AccountRepository accountRepository;
	
	@Transactional(readOnly=true)
	public AccountRes getAccountInfo(String username) {
		return accountRepository.findByUsername(username)
				.map(AccountRes::of)
				.orElseThrow(() -> new RuntimeException("유저 정보가 없습니다."));
	}
	
	// 현재 SecurityContext 에 있는 유저 정보 가져오기
	@Transactional(readOnly = true)
    public AccountRes getMyInfo() {
        return accountRepository.findById(SecurityUtil.getCurrentAccountId())
                .map(AccountRes::of)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다."));
    }
	
	
}
