package com.siang.security.server.provider;

import com.siang.security.server.model.ApiToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class TwjugAuthenticationProvider implements AuthenticationProvider {
    private static final Logger logger = LoggerFactory.getLogger(TwjugAuthenticationProvider.class);

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        logger.info("return Authentication: {}", authentication);
        return authentication;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return ApiToken.class.equals(aClass);
    }
}
