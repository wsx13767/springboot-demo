package com.evolutivelabs.app.counter.gateway.config.auth;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class SecurityContextRepository implements ServerSecurityContextRepository {
    private static final Logger logger = LoggerFactory.getLogger(SecurityContextRepository.class);
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return null;
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {

        List<HttpCookie> cookieList = exchange.getRequest().getCookies().get("token");
        if (cookieList != null && cookieList.size() > 0) {
            if (StringUtils.isNotBlank(cookieList.get(0).getValue())) {
                return Mono.justOrEmpty(cookieList.get(0).getValue())
                        .flatMap(token ->
                                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(token, token))
                                        .map(SecurityContextImpl::new));
            }
        }

        return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                .filter(header -> header.startsWith("Bearer "))
                .flatMap(header -> {
                    String token = header.substring(7);
                    Authentication auth = new UsernamePasswordAuthenticationToken(token, token);
                    return authenticationManager.authenticate(auth).map(SecurityContextImpl::new);
                });
    }
}
