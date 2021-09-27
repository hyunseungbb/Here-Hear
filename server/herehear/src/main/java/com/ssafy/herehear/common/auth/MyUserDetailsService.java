package com.ssafy.herehear.common.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.ssafy.herehear.api.service.AccountService;
import com.ssafy.herehear.db.entity.Account;

/**
 * 현재 액세스 토큰으로 부터 인증된 유저의 상세정보(활성화 여부, 만료, 롤 등) 관련 서비스 정의.
 */
@Component
public class MyUserDetailsService implements UserDetailsService{
	@Autowired
	AccountService accountService;
	
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    		Account account = accountService.getAccount(username);
    		if(account != null) {
    			MyUserDetails userDetails = new MyUserDetails(account);
    			return userDetails;
    		}
    		return null;
    }
}
