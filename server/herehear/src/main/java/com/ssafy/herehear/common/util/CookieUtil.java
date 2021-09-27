package com.ssafy.herehear.common.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

// access token을 cookie 형태로 저장
@Service
public class CookieUtil {

    public Cookie createCookie(String cookieName, String value){
        Cookie token = new Cookie(cookieName,value);
        token.setHttpOnly(true);
        token.setMaxAge((int)JwtTokenUtil.TOKEN_VALIDATION_SECOND);
        token.setPath("/");
        return token;
    }
    
    public Cookie LogoutCookie(String cookieName, String value){
    	Cookie token = new Cookie(cookieName,value);
    	token.setHttpOnly(true);
    	token.setMaxAge(0);
    	token.setPath("/");
    	return token;
    }

    public Cookie getCookie(HttpServletRequest req, String cookieName){
        final Cookie[] cookies = req.getCookies();
        if(cookies==null) return null;
        for(Cookie cookie : cookies){
            if(cookie.getName().equals(cookieName))
                return cookie;
        }
        return null;
    }
    
}
