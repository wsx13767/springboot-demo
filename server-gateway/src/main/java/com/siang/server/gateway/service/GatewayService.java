package com.siang.server.gateway.service;

import com.google.common.collect.Lists;
import com.siang.server.gateway.database.entity.ApiRoute;
import com.siang.server.gateway.database.repository.ApiRouterRepository;
import com.siang.server.gateway.model.GatewayRequest;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
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
    public List<ApiRoute> findApiRoutes() {
        logger.info("find all router");
        return Lists.newArrayList(apiRouterRepository.findAll());
    }

    public ApiRoute findApiRoute(Integer id) {
        return apiRouterRepository.findById(id).get();
    }

    @CacheEvict(key = "'all'")
    public ApiRoute createApiRouter(GatewayRequest request) {
        ApiRoute apiRoute = new ApiRoute(request.getApiId(), request.getUri(), request.getHostName(),
                request.getPath(), null, null, false);

        return apiRouterRepository.save(apiRoute);
    }

    @CacheEvict(key = "'all'")
    public ApiRoute updateApiRouter(Integer id, GatewayRequest request) {
         return apiRouterRepository
                 .findById(id)
                 .map(apiRouter -> {
                     if (StringUtils.isNotBlank(request.getApiId())) {
                         apiRouter.setApiId(request.getApiId());
                     }
                     if (StringUtils.isNotBlank(request.getUri())) {
                         apiRouter.setUri(request.getUri());
                     }
                     if (StringUtils.isNotBlank(request.getPath())) {
                         apiRouter.setPath(request.getPath());
                     }
                     if (StringUtils.isNotBlank(request.getHostName())) {
                         apiRouter.setHostName(request.getHostName());
                     }
                     if (StringUtils.isNotBlank(request.getBefore())) {
                         apiRouter.setBefore(request.getBeforeTime());
                     }
                     if (StringUtils.isNotBlank(request.getAfter())) {
                         apiRouter.setAfter(request.getAfterTime());
                     }
                     if (request.getStatus() != null) {
                         apiRouter.setStatus(request.getStatus());
                     }
                     return apiRouter;
                 })
                 .map(apiRouterRepository::save).get();
    }

    @CacheEvict(key = "'all'")
    public void deleteRouter(Integer id) {
        apiRouterRepository
                .findById(id)
                .ifPresent(apiRouterRepository::delete);
    }
}
