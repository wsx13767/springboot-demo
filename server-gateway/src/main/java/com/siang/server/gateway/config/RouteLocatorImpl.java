package com.siang.server.gateway.config;

import com.siang.server.gateway.database.entity.ApiRouter;
import com.siang.server.gateway.database.repository.ApiRouterRepository;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.*;
import reactor.core.publisher.Flux;

public class RouteLocatorImpl implements RouteLocator {
    private RouteLocatorBuilder builder;
    private ApiRouterRepository apiRouterRepository;

    public RouteLocatorImpl(RouteLocatorBuilder builder, ApiRouterRepository apiRouterRepository) {
        this.builder = builder;
        this.apiRouterRepository = apiRouterRepository;
    }

    @Override
    public Flux<Route> getRoutes() {
        RouteLocatorBuilder.Builder routes = builder.routes();
        Flux<ApiRouter> apiRouters = apiRouterRepository.findAll();
        return apiRouters.map(apiRouter -> routes.route(apiRouter.getApiId(),
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
