package com.ssafy.herehear.api.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.herehear.api.request.AccountReq;
import com.ssafy.herehear.api.response.AccountRes;
import com.ssafy.herehear.common.jwt.TokenDto;
import com.ssafy.herehear.common.jwt.TokenProvider;
import com.ssafy.herehear.common.jwt.TokenReqDto;
import com.ssafy.herehear.common.util.RedisUtil;
import com.ssafy.herehear.db.entity.Account;
import com.ssafy.herehear.db.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RedisUtil redisUtil;
    
    @Transactional
    public AccountRes signup(AccountReq accountReq) {
        if (accountRepository.existsByUsername(accountReq.getUsername())) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다");
        }

        Account account = accountReq.toAccount(passwordEncoder);
        return AccountRes.of(accountRepository.save(account));
    }
    
    @Transactional
    public TokenDto login(AccountReq accountReq) {
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = accountReq.toAuthentication();

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 4. RefreshToken 저장
        redisUtil.setDataExpire(authentication.getName(), tokenDto.getRefreshToken(), 1000 * 60 * 60 * 24 * 7);

        // 5. 토큰 발급
        return tokenDto;
    }
    
    @Transactional
    public TokenDto reissue(TokenReqDto toeknReqDto) {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(toeknReqDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 username 가져오기
        Authentication authentication = tokenProvider.getAuthentication(toeknReqDto.getAccessToken());

        // 3. redis 저장소에서 username 를 기반으로 Refresh Token 값 가져옴
        String refreshToken = redisUtil.getData(authentication.getName())
        		.orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.equals(toeknReqDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 6. 저장소 정보 업데이트
        redisUtil.setDataExpire(authentication.getName(), tokenDto.getRefreshToken(), 1000 * 60 * 60 * 24 * 7);

        // 토큰 발급
        return tokenDto;
    }
}
