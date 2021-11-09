package com.siang.server.gateway.service;

import com.siang.server.gateway.database.entity.ApiRouter;
import com.siang.server.gateway.database.repository.ApiRouterRepository;
import com.siang.server.gateway.model.GatewayRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GatewayService {
    @Autowired
    private ApiRouterRepository apiRouterRepository;

    public Flux<ApiRouter> findApiRoutes() {
        return apiRouterRepository.findAll();
    }

    public Mono<ApiRouter> findApiRoute(Integer id) {
        return findAndValidateApiRouter(id);
    }

    public Mono<Void> createApiRouter(GatewayRequest request) {
        ApiRouter apiRouter = setNewApiRouter(new ApiRouter(), request);
        return apiRouterRepository.save(apiRouter).then();
    }

    public Mono<Void> updateApiRouter(Integer id, GatewayRequest request) {
        return findAndValidateApiRouter(id)
                .map(apiRouter -> setNewApiRouter(apiRouter, request))
                .flatMap(apiRouterRepository::save)
                .then();
    }

    public Mono<Void> deleteRouter(Integer id) {
        return findAndValidateApiRouter(id)
                .flatMap(apiRouterRepository::delete);
    }

    private Mono<ApiRouter> findAndValidateApiRouter(Integer id) {
        return apiRouterRepository.findById(id)
                .switchIfEmpty(Mono.error(
                        new RuntimeException(String.format("Api route with id %d not found", id))
                ));
    }

    private ApiRouter setNewApiRouter(ApiRouter apiRouter, GatewayRequest request) {
        apiRouter.setApiId(request.getApiId());
        apiRouter.setHostName(request.getHostName());
        apiRouter.setUri(request.getUri());
        apiRouter.setPath(request.getPath());
        return apiRouter;
    }
}
