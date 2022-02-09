package com.evolutivelabs.app.counter.gateway.config;

import com.evolutivelabs.app.counter.database.mysql.RepositoryFactory;
import com.evolutivelabs.app.counter.database.mysql.entity.ApiServer;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Primary
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SwaggerConfig implements SwaggerResourcesProvider {
//    private final RouteLocator routeLocator;
    private final RepositoryFactory repositoryFactory;

    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();

        repositoryFactory.getApiRouteRepository().findDocsByStatus(true, true)
                .forEach(apiRoute -> {
                    Optional<ApiServer> apiServerOptional = repositoryFactory.getApiServerRepository().findById(apiRoute.getServerId());
                    apiServerOptional.ifPresent(apiServer ->
                            resources.add(swaggerResource(apiRoute.getName(), "/" + apiServer.getGroupId() + "/" + apiServer.getId() + apiRoute.getUri(), "1.0")));
                });
//        routeLocator.getRoutes().subscribe(route -> {
//            if (route.getId().endsWith("SWAGGER_DOCS")) {
//                resources.add(swaggerResource(name, route.getPredicate()., "1.0"));
//            }
//        });
        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location, String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }
}
