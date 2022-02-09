package com.evolutivelabs.app.counter.gateway.service;

import com.evolutivelabs.app.counter.database.mysql.RepositoryFactory;
import com.evolutivelabs.app.counter.database.mysql.entity.ApiGroup;
import com.evolutivelabs.app.counter.database.mysql.entity.ApiRoute;
import com.evolutivelabs.app.counter.database.mysql.entity.ApiServer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.BooleanSpec;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

public class RouteLocatorImpl implements RouteLocator {
    private final RouteLocatorBuilder builder;
    private final RepositoryFactory repositoryFactory;

    public RouteLocatorImpl(RouteLocatorBuilder builder, RepositoryFactory repositoryFactory) {
        this.builder = builder;
        this.repositoryFactory = repositoryFactory;
    }

    @Override
    public Flux<Route> getRoutes() {
        RouteLocatorBuilder.Builder routes = builder.routes();
        List<ApiGroup> apiGroups = this.repositoryFactory.getApiGroupRepository().findByStatus(true);
        for (ApiGroup apiGroup : apiGroups) {
            List<ApiServer> apiServers = this.repositoryFactory.getApiServerRepository().findByGroupIdAndStatus(apiGroup.getId(), true);
            for (ApiServer apiServer : apiServers) {
                List<ApiRoute> apiRoutes = this.repositoryFactory.getApiRouteRepository().findByServerIdAndStatus(apiServer.getId(), true);
                apiRoutes.forEach(apiRoute -> routes.route(apiRoute.getServerId() + "_" + apiRoute.getId(),
                        fn -> setPredicateSpec(fn, apiRoute, apiServer, apiGroup)));
            }
        }

        return routes.build().getRoutes();
    }

    private Buildable<Route> setPredicateSpec(PredicateSpec predicateSpec, ApiRoute apiRoute,
                                              ApiServer apiServer, ApiGroup apiGroup) {
        String path = "/" + apiGroup.getId() + "/" + apiServer.getId() + apiRoute.getUri();
        BooleanSpec booleanSpec = predicateSpec.path(path);
        if (apiRoute.getBefore() != null) {
            booleanSpec = booleanSpec.and().before(apiRoute.getBefore().atZone(ZoneId.systemDefault()));
        }

        if (apiRoute.getAfter() != null) {
            booleanSpec = booleanSpec.and().after(apiRoute.getAfter().atZone(ZoneId.systemDefault()));
        }
        return booleanSpec.filters(f -> f.rewritePath("/" + apiGroup.getId() + "/" + apiServer.getId() + "/?(?<remaining>.*)", "/${remaining}"))
                .uri(apiServer.getHost());
    }
}
