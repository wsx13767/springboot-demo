package com.evolutivelabs.app.counter.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

//@Component
public class AuthFilter implements WebFilter {
    private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        logger.info("uri: {}", exchange.getRequest().getURI());
        logger.info("path: {}", exchange.getRequest().getPath());
        logger.info("this is webFilter");
        return chain.filter(exchange);
    }
}
