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
    Logger logger = LoggerFactory.getLogger(GatewayConfig.class);
    @Autowired
    private ApiRouterRepository apiRouterRepository;

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder routeLocatorBuilder) {

        Flux<ApiRouter> apiRouters = apiRouterRepository.findAll();
        return new RouteLocator() {
            @Override
            public Flux<Route> getRoutes() {
                RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();
                return apiRouters.map(apiRouter -> routes.route(apiRouter.getApiId(),
                        predicateSpec -> predicateSpec.path("/" + apiRouter.getHostName() + apiRouter.getPath())
                                .filters(f -> f.rewritePath("/" + apiRouter.getHostName() + "/?(?<remaining>.*)", "/${remaining}"))
                                .uri(apiRouter.getUri())
                )).collectList().flatMapMany(builders -> routes.build().getRoutes());
            }
        };
//        apiRouters.
//        for (ApiRouter apiRouter : apiRouters) {
//
//        }

//        routes.route("providerModule", predicateSpec -> {
//            return predicateSpec
//                    .path("/api/provider/**")
//                    // 為何要加上這段才能正常mapping?
//                    .filters(f -> f.rewritePath("/api/provider/?(?<remaining>.*)", "/${remaining}"))
//                    .uri("lb://SERVER-PROVIDER");
//        });
//
//        return routes.build();
    }
}
