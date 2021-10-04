package com.ssafy.herehear.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.filter.CorsFilter;

import com.ssafy.herehear.common.jwt.JwtAccessDeniedHandler;
import com.ssafy.herehear.common.jwt.JwtAuthenticationEntryPoint;
import com.ssafy.herehear.common.jwt.TokenProvider;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	private final CorsFilter corsFilter;
	private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
	
	// Password 인코딩 방식에 BCrypt 암호화 방식 사용
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    // swagger 테스트가 원활하도록 관련 API 들은 전부 무시
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
            .antMatchers("/v2/api-docs",
            			"/configuration/ui",		                   
            			"/swagger-resources/**",
	                    "/configuration/security",
	                    "/swagger-ui.html",
	                    "/webjars/**");
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // CSRF 설정 Disable
    	http.csrf().disable()
    	
    		// exception hanlding 할때 직접 만든 클래스 추가
    		.exceptionHandling()
    		.authenticationEntryPoint(jwtAuthenticationEntryPoint)
    		.accessDeniedHandler(jwtAccessDeniedHandler)
    		
    		// 시큐리티는 기본적으로 세션 사용
    		// 근데 우리는 세션 사용을 안할것이므로 stateless로 설정
    		.and()
    		.sessionManagement()
    		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    		
    		// 로그인, 회원가입 API는 토큰 없어도 요청이 가능해야 함
    		.and()
    		.authorizeRequests()
    		.antMatchers("/api/v1/auth/**").permitAll()
    		.antMatchers("/swagger-ui/**").permitAll()
    		.anyRequest().authenticated()	// 나머지 API는 전부 인증 필요
    		
    		// JwtFilter를 addFilterBefore로 등록했던 JwtSecurityConfig 클래스 적용
    		.and()
    		.apply(new JwtSecurityConfig(tokenProvider));
    }
}
