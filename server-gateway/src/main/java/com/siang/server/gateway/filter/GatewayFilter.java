package com.siang.server.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class GatewayFilter implements GlobalFilter, Ordered {
    private static final Logger logger = LoggerFactory.getLogger(GatewayFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logger.info("********    global filter : {}  *********", exchange.getRequest().getURI().getPath());
//        if (!exchange.getRequest().getURI().getPath().startsWith("/admin")) {
//            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
//                ServerHttpResponse response = exchange.getResponse();
//                response.setStatusCode(HttpStatus.UNAUTHORIZED);
//                return response.setComplete();
//            }
//        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
