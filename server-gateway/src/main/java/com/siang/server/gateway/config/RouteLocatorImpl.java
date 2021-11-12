package com.siang.server.gateway.config;

import com.siang.server.gateway.database.entity.ApiRouter;
import com.siang.server.gateway.service.GatewayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.*;
import reactor.core.publisher.Flux;

import java.util.List;

public class RouteLocatorImpl implements RouteLocator {
    private static final Logger logger = LoggerFactory.getLogger(RouteLocatorImpl.class);

    private RouteLocatorBuilder builder;
    private GatewayService gatewayService;

    public RouteLocatorImpl(RouteLocatorBuilder builder, GatewayService gatewayService) {
        this.builder = builder;
        this.gatewayService = gatewayService;
    }

    @Override
    public Flux<Route> getRoutes() {
        RouteLocatorBuilder.Builder routes = builder.routes();
        List<ApiRouter> apiRouters = gatewayService.findApiRoutes();
        logger.info("==============查詢router=============");
        return Flux.fromIterable(apiRouters).map(apiRouter -> routes.route(apiRouter.getApiId(),
                        predicateSpec -> setPredicateSpec(apiRouter, predicateSpec)))
                .collectList()
                .flatMapMany(builders -> routes.build().getRoutes());
    }

    private Buildable<Route> setPredicateSpec(ApiRouter apiRouter, PredicateSpec predicateSpec) {
        String path = "/".concat(apiRouter.getHostName()).concat(apiRouter.getPath());
        BooleanSpec booleanSpec = predicateSpec.path(path);

        UriSpec uriSpec = booleanSpec.filters(f -> f.rewritePath(
                "/".concat(apiRouter.getHostName()).concat("/?(?<remaining>.*)"),
                "/${remaining}"
        ));

        return uriSpec.uri(apiRouter.getUri());
    }
}
