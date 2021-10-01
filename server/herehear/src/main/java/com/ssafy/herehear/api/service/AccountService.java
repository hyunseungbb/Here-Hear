package com.ssafy.herehear.api.service;

import com.ssafy.herehear.api.response.AccountRes;

/**
 *	유저 관련 비즈니스 로직 처리를 위한 서비스 인터페이스 정의.
 */
public interface AccountService {
	
	AccountRes getAccountInfo(String username);
	AccountRes getMyInfo();
	
//	@Autowired
//	private AccountRepository AccountRepository;
//	public Optional<Account> findById(Long id) { 
//		Optional<Account> user = AccountRepository.findById(id); 
//		return user; 
//	}
}
