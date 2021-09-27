package com.ssafy.herehear.api.controller;

import java.security.Principal;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.herehear.api.request.AccountLoginPostReq;
import com.ssafy.herehear.api.response.AccountLoginPostRes;
import com.ssafy.herehear.api.response.BaseResponseBody;
import com.ssafy.herehear.api.service.AccountService;
import com.ssafy.herehear.common.auth.MyUserDetails;
import com.ssafy.herehear.common.auth.MyUserDetailsService;
import com.ssafy.herehear.common.util.CookieUtil;
import com.ssafy.herehear.common.util.JwtTokenUtil;
import com.ssafy.herehear.common.util.RedisUtil;
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
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
    private CookieUtil cookieUtil;
	
	@Autowired
	private RedisUtil redisUtil;
	
	@PostMapping("/login")
	@ApiOperation(value = "로그인")
	public ResponseEntity<AccountLoginPostRes> login(
			@RequestBody @ApiParam(value="로그인 정보", required=true) AccountLoginPostReq loginInfo, HttpServletResponse res) {
		String username = loginInfo.getUsername();
		String password = loginInfo.getPassword();
		
		Account account = accountService.getAccount(username);
		
		// 로그인 요청한 유저로부터 유효한 패스워드인지 확인
		if (passwordEncoder.matches(password, account.getPassword())) {
			// 유효한 패스워드인 경우, 로그인 성공
			final String jwtToken = jwtTokenUtil.generateToken(account);
			final String refreshJwt = jwtTokenUtil.generateRefreshToken(account);
			
			// 새로 생긴 토큰을 쿠키로 변환
			Cookie accessToken = cookieUtil.createCookie(JwtTokenUtil.ACCESS_TOKEN_NAME, jwtToken);
            Cookie refreshToken = cookieUtil.createCookie(JwtTokenUtil.REFRESH_TOKEN_NAME, refreshJwt);

            // 쿠키에 추가 (안드로이드에서 필요없으면 제거 예정)
            res.addCookie(accessToken);
            res.addCookie(refreshToken);
            
            // redis에 key: username, value: refreshToken 추가
            redisUtil.setDataExpire(account.getUsername(), refreshJwt, JwtTokenUtil.REFRESH_TOKEN_VALIDATION_SECOND);
            
            // 리턴 값으로도 토큰 전달 jwtToken 사용
			return ResponseEntity.ok(AccountLoginPostRes.of(200, "Success", jwtToken, refreshJwt));
		}
		
		// 유효하지 않은 경우, 로그인 실패
		return ResponseEntity.status(401).body(AccountLoginPostRes.of(401, "Invalid Password", null, null));
	}
	
	@GetMapping("/logout")
	@ApiOperation(value = "로그아웃")
	public ResponseEntity<?> logout(HttpServletRequest req, HttpServletResponse res) {
		
		// 현재 Access토큰을 기반으로 유저 확인
		Cookie jwtToken = cookieUtil.getCookie(req, jwtTokenUtil.ACCESS_TOKEN_NAME);		
		String jwt = jwtToken.getValue();
		String username = jwtTokenUtil.getUsername(jwt);
		
		// Cookie refreshToken = cookieUtil.getCookie(req, JwtTokenUtil.REFRESH_TOKEN_NAME);
		
		// 만료시간이 0이고 token값을 빈 값으로 하여 쿠키 발급
		Cookie nullAccessToken = cookieUtil.LogoutCookie(JwtTokenUtil.ACCESS_TOKEN_NAME, "");
        Cookie nullRefreshToken = cookieUtil.LogoutCookie(JwtTokenUtil.REFRESH_TOKEN_NAME, "");
        
        // 쿠키에 추가 -> 만료시간이 0이므로 바로 만료
		res.addCookie(nullAccessToken);
		res.addCookie(nullRefreshToken);
		
		// redis에 저장되어 있는 유저의 refreshToken 제거
		redisUtil.deleteData(username);

		return ResponseEntity.ok("로그아웃 성공");
	}
}
