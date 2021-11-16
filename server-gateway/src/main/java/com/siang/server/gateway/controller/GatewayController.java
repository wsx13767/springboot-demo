package com.siang.server.gateway.controller;

import com.siang.server.gateway.database.entity.ApiRoute;
import com.siang.server.gateway.database.entity.RouteGroup;
import com.siang.server.gateway.model.apiRoute.ApiRouteRequest;
import com.siang.server.gateway.model.apiRoute.ApiRouteResponse;
import com.siang.server.gateway.model.settings.ApiServerResponse;
import com.siang.server.gateway.model.settings.RouteGroupResponse;
import com.siang.server.gateway.service.GatewayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/internal/gateway/api-routes")
public class GatewayController {
    private static final Logger logger = LoggerFactory.getLogger(GatewayController.class);
    @Autowired
    private GatewayService gatewayService;
    @Autowired
    private ConcurrentMapCacheManager cacheManager;

    @GetMapping("/all")
    public List<RouteGroupResponse> findAllSettings() {
        List<RouteGroupResponse> responses = new ArrayList<>();
        List<RouteGroup> routeGroups = gatewayService.findGroups();
        routeGroups.forEach(
                routeGroup -> {
                    RouteGroupResponse response = new RouteGroupResponse(routeGroup);
                    response.setApiServers(gatewayService.findServerByGroupId(routeGroup.getId()).stream().map(
                            apiServer -> {
                                ApiServerResponse apiServerResponse = new ApiServerResponse(apiServer);
                                apiServerResponse.setApiRoutes(gatewayService.findApiRoutesByServerId(apiServer.getId()));
                                return apiServerResponse;
                            }
                    ).collect(Collectors.toList()));
                    responses.add(response);
                }
        );
        return responses;
    }

    @GetMapping
    public List<ApiRouteResponse> findApiRoutes() {
        return gatewayService.findApiRoutes().stream()
                .map(this::convertApiRoute)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ApiRouteResponse findApiRoute(@PathVariable Integer id) {
        ApiRouteResponse response = new ApiRouteResponse();
        ApiRoute route = gatewayService.findApiRouteById(id);
        return convertApiRoute(route);
    }

    @PutMapping("/{id}")
    public ApiRouteResponse updateApiRoute(@PathVariable Integer id, @RequestBody ApiRouteRequest request) {
        return convertApiRoute(gatewayService.updateApiRouter(id, request));
    }

    private ApiRouteResponse convertApiRoute(ApiRoute route) {
        ApiRouteResponse response = new ApiRouteResponse();
        response.setServerId(route.getServerId());
        response.setPath(route.getPath());
        response.setStatus(route.getStatus());
        response.setDesc(route.getDesc());
        response.setBefore(route.getBefore());
        response.setAfter(route.getAfter());
        return response;
    }

    @GetMapping("/cacheManager")
    public Map<String, Map<Object, Object>> getManager() {
        Map<String, Map<Object, Object>> result = new HashMap<>();
        cacheManager.getCacheNames().stream().peek(
                cacheName -> {
                    logger.info("cacheName: {}", cacheName);
                    result.put(cacheName, (Map) cacheManager.getCache(cacheName).getNativeCache());
                }
        ).count();

        return result;
    }
}
