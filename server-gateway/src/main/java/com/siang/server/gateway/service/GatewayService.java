package com.siang.server.gateway.service;

import com.google.common.collect.Lists;
import com.siang.server.gateway.database.entity.ApiRouter;
import com.siang.server.gateway.database.repository.ApiRouterRepository;
import com.siang.server.gateway.model.GatewayRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "apiRouterCahce")
public class GatewayService {
    private static final Logger logger = LoggerFactory.getLogger(GatewayService.class);
    @Autowired
    private ApiRouterRepository apiRouterRepository;

    @Cacheable(key = "'all'")
    public List<ApiRouter> findApiRoutes() {
        logger.info("find all router");
        return Lists.newArrayList(apiRouterRepository.findAll());
    }

    public ApiRouter findApiRoute(Integer id) {
        return apiRouterRepository.findById(id).get();
    }

    @CacheEvict(key = "'all'")
    public ApiRouter createApiRouter(GatewayRequest request) {
        ApiRouter apiRouter = setNewApiRouter(new ApiRouter(), request);
        return apiRouterRepository.save(apiRouter);
    }

    @CacheEvict(key = "'all'")
    public ApiRouter updateApiRouter(Integer id, GatewayRequest request) {
         return apiRouterRepository
                 .findById(id)
                 .map(apiRouter -> setNewApiRouter(apiRouter, request))
                 .map(apiRouterRepository::save).get();
    }

    @CacheEvict(key = "'all'")
    public void deleteRouter(Integer id) {
        apiRouterRepository
                .findById(id)
                .ifPresent(apiRouterRepository::delete);
    }

    private ApiRouter setNewApiRouter(ApiRouter apiRouter, GatewayRequest request) {
        apiRouter.setApiId(request.getApiId());
        apiRouter.setHostName(request.getHostName());
        apiRouter.setUri(request.getUri());
        apiRouter.setPath(request.getPath());
        return apiRouter;
    }
}
