package com.siang.server.gateway.controller;

import com.siang.server.gateway.database.entity.ApiRouter;
import com.siang.server.gateway.model.GatewayRequest;
import com.siang.server.gateway.model.GatewayResponse;
import com.siang.server.gateway.service.GatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@RestController
@RequestMapping("/internal/gateway/api-routes")
public class GatewayController {
    @Autowired
    private GatewayService gatewayService;

    @GetMapping
    public Mono<List<GatewayResponse>> findApiRoutes() {
        return gatewayService.findApiRoutes()
                .map(this::convert)
                .collectList()
                .subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping("/{id}")
    public Mono<GatewayResponse> findApiRouter(@PathVariable Integer id) {
        return gatewayService.findApiRoute(id)
                .map(this::convert)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @PostMapping
    public Mono<?> createApiRouter(@RequestBody @Validated GatewayRequest request) {
        return gatewayService.createApiRouter(request)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @PutMapping("/{id}")
    public Mono<?> updateApiRouter(@PathVariable Integer id, @RequestBody @Validated GatewayRequest request) {
        return gatewayService.updateApiRouter(id, request)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @DeleteMapping("/{id}")
    public Mono<?> deleteApiRouter(@PathVariable Integer id) {
        return gatewayService.deleteRouter(id)
                .subscribeOn(Schedulers.boundedElastic());
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
