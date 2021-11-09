package com.siang.server.gateway.config;

import com.siang.server.gateway.database.entity.ApiRouter;
import com.siang.server.gateway.database.repository.ApiRouterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Configuration
public class GatewayConfig {
    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder routeLocatorBuilder, ApiRouterRepository apiRouterRepository) {
        return new RouteLocatorImpl(routeLocatorBuilder, apiRouterRepository);
    }
}
