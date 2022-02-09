package com.evolutivelabs.app.counter.gateway.config.auth;

import com.evolutivelabs.app.counter.gateway.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationManager.class);

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getPrincipal().toString();
        Map<String, String> userInfo = JwtUtils.verify(authToken);


        return Mono.just(new UsernamePasswordAuthenticationToken(userInfo.get("name"), null,
                Arrays.stream(userInfo.get("roles").split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList())));
    }
}
