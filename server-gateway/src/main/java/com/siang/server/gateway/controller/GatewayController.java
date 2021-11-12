package com.siang.server.gateway.controller;

import com.siang.server.gateway.database.entity.ApiRouter;
import com.siang.server.gateway.model.GatewayRequest;
import com.siang.server.gateway.model.GatewayResponse;
import com.siang.server.gateway.service.GatewayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Iterator;
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

    @GetMapping
    public List<GatewayResponse> findApiRoutes() {
        return gatewayService.findApiRoutes().stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public GatewayResponse findApiRouter(@PathVariable Integer id) {
        return convert(gatewayService.findApiRoute(id));
    }

    @PostMapping
    public GatewayResponse createApiRouter(@RequestBody @Validated GatewayRequest request) {
        return convert(gatewayService.createApiRouter(request));
    }

    @PutMapping("/{id}")
    public GatewayResponse updateApiRouter(@PathVariable Integer id, @RequestBody @Validated GatewayRequest request) {
        return convert(gatewayService.updateApiRouter(id, request));
    }

    @DeleteMapping("/{id}")
    public String deleteApiRouter(@PathVariable Integer id) {
        gatewayService.deleteRouter(id);

        return "delete success";
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

    private GatewayResponse convert(ApiRouter apiRouter) {
        GatewayResponse response = new GatewayResponse();
        response.setId(apiRouter.getId());
        response.setApiId(apiRouter.getApiId());
        response.setUri(apiRouter.getUri());
        response.setHostName(apiRouter.getHostName());
        response.setPath(apiRouter.getPath());
        return response;
    }
}
