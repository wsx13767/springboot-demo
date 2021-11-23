package com.siang.security.server.provider;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class KaptchaAuthenticationProvider extends DaoAuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String kaptcha = req.getParameter("kaptcha");
        String sessionKaptcha = (String) req.getSession().getAttribute("kaptcha");
        if (kaptcha != null && sessionKaptcha != null
            && kaptcha.equalsIgnoreCase(sessionKaptcha)) {
            super.authenticate(authentication);
        }
        throw new AuthenticationServiceException("驗證碼錯誤");
    }
}
