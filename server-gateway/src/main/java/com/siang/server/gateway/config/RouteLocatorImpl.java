package com.siang.server.gateway.config;

import com.siang.server.gateway.database.entity.ApiRoute;
import com.siang.server.gateway.database.entity.ApiServer;
import com.siang.server.gateway.database.entity.RouteGroup;
import com.siang.server.gateway.service.GatewayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.*;
import reactor.core.publisher.Flux;

import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

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
        gatewayService.findGroups().stream()
                .filter(route -> route.getStatus())
                .forEach(routeGroup ->
                        gatewayService.findServerByGroupId(routeGroup.getId()).stream()
                                .filter(apiServer -> apiServer.getStatus())
                                .forEach(apiServer ->
                                        gatewayService.findApiRoutesByServerId(apiServer.getId()).stream()
                                                .filter(apiRoute -> apiRoute.getStatus())
                                                .forEach(apiRoute ->
                                                        routes.route(apiRoute.getServerId().concat(apiRoute.getId().toString()),
                                                                predicateSpec -> setPredicateSpec(apiRoute, apiServer, routeGroup, predicateSpec))
                                                )));

        return routes.build().getRoutes();

//        List<ApiRoute> apiRoutes = gatewayService.findApiRoutes().stream().filter(apiRoute -> apiRoute.getStatus())
//                .collect(Collectors.toList());
//        logger.info("查詢routes...");
//
//        for (ApiRoute apiRoute : apiRoutes) {
//            routes.route(apiRoute.getApiId(), predicateSpec -> setPredicateSpec(apiRoute, predicateSpec));
//        }
//        return routes.build().getRoutes();


//        List<RouteLocatorBuilder.Builder> test = Flux.fromIterable(apiRouters).map(
//                apiRouter -> routes.route(
//                        apiRouter.getApiId(),
//                        predicateSpec -> setPredicateSpec(apiRouter, predicateSpec)
//                )
//        ).collectList().block();
//        return
//                .flatMapMany(builders -> routes.build().getRoutes());
    }

    private Buildable<Route> setPredicateSpec(ApiRoute apiRoute, ApiServer apiServer, RouteGroup routeGroup, PredicateSpec predicateSpec) {
        String path = "/" + routeGroup.getId() + "/" + apiServer.getName() + apiRoute.getPath();
        BooleanSpec booleanSpec = predicateSpec.path(path);
        // 此api只能在幾點幾分之前使用
        if (apiRoute.getBefore() != null) {
            booleanSpec = booleanSpec.and().before(apiRoute.getBefore().atZone(ZoneId.systemDefault()));
        }
        // 此api只能在幾點幾分之後使用
        if (apiRoute.getAfter() != null) {
            booleanSpec = booleanSpec.and().after(apiRoute.getAfter().atZone(ZoneId.systemDefault()));
        }

        UriSpec uriSpec = booleanSpec.filters(f ->
                f.rewritePath(
                        "/" + routeGroup.getId() + "/" + apiServer.getName() + "/?(?<remaining>.*)",
                        "/${remaining}"
                ));
        return uriSpec.uri(apiServer.getUri());
    }
//
//    private Buildable<Route> setPredicateSpec(ApiRoute apiRoute, PredicateSpec predicateSpec) {
//        String path = "/api/".concat(apiRoute.getHostName()).concat(apiRoute.getPath());
//        BooleanSpec booleanSpec = predicateSpec.path(path);
//
//        // 此api只能在幾點幾分之前使用
//        if (apiRoute.getBefore() != null) {
//            booleanSpec = booleanSpec.and().before(apiRoute.getBefore().atZone(ZoneId.systemDefault()));
//        }
//        // 此api只能在幾點幾分之後使用
//        if (apiRoute.getAfter() != null) {
//            booleanSpec = booleanSpec.and().after(apiRoute.getAfter().atZone(ZoneId.systemDefault()));
//        }
//
//        UriSpec uriSpec = booleanSpec.filters(f -> f.rewritePath(
//                "/api/".concat(apiRoute.getHostName()).concat("/?(?<remaining>.*)"),
//                "/${remaining}"
//        ));
//
//        return uriSpec.uri(apiRoute.getUri());
//    }
}
