package com.siang.server.gateway.config;

import com.siang.server.gateway.service.GatewayService;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder routeLocatorBuilder, GatewayService gatewayService) {
        return new RouteLocatorImpl(routeLocatorBuilder, gatewayService);
    }
}
