package com.siang.server.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes().route("providerModule", predicateSpec -> {
            return predicateSpec
                    .path("/provider/**")
                    // 為何要加上這段才能正常mapping?
                    .filters(f -> f.rewritePath("/provider/?(?<remaining>.*)", "/${remaining}"))
                    .uri("lb://SERVER-PROVIDER");
        }).build();
    }
}
