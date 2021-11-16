package com.siang.server.gateway.service;

import com.google.common.collect.Lists;
import com.siang.server.gateway.database.entity.ApiRoute;
import com.siang.server.gateway.database.entity.ApiServer;
import com.siang.server.gateway.database.entity.RouteGroup;
import com.siang.server.gateway.database.repository.ApiRouterRepository;
import com.siang.server.gateway.database.repository.ApiServerRepository;
import com.siang.server.gateway.database.repository.RouteGroupRepository;
import com.siang.server.gateway.model.apiRoute.ApiRouteRequest;
import org.apache.commons.lang.StringUtils;
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
@CacheConfig(cacheNames = "GatewayRoutesConfig")
public class GatewayService {
    private static final Logger logger = LoggerFactory.getLogger(GatewayService.class);
    @Autowired
    private ApiRouterRepository apiRouterRepository;
    @Autowired
    private ApiServerRepository apiServerRepository;
    @Autowired
    private RouteGroupRepository routeGroupRepository;

    @Cacheable(key = "'Gateway_groups'")
    public List<RouteGroup> findGroups() {
        return Lists.newArrayList(routeGroupRepository.findAll());
    }

    @Cacheable(key = "'GatewayApiServer.' + #groupId")
    public List<ApiServer> findServerByGroupId(String groupId) {
        return apiServerRepository.findByGroupId(groupId);
    }

    @Cacheable(key = "'GateApiRoute.' + #serverId")
    public List<ApiRoute> findApiRoutesByServerId(String serverId) {
        return apiRouterRepository.findByServerId(serverId);
    }

    public ApiRoute findApiRouteById(Integer id) {
        return apiRouterRepository.findById(id).get();
    }

    public List<ApiRoute> findApiRoutes() {
        return Lists.newArrayList(apiRouterRepository.findAll());
    }

    @CacheEvict
    public ApiRoute createApiRouter(ApiRouteRequest request) {
        ApiRoute apiRoute = new ApiRoute();
        apiRoute.setPath(request.getPath());
        apiRoute.setBefore(request.getBeforeTime());
        apiRoute.setAfter(request.getAfterTime());
        apiRoute.setServerId(request.getServerId());
        apiRoute.setDesc(request.getDesc());
        apiRoute.setStatus(request.getStatus());

        return apiRouterRepository.save(apiRoute);
    }

    @CacheEvict(allEntries = true)
    public ApiRoute updateApiRouter(Integer id, ApiRouteRequest request) {
         return apiRouterRepository
                 .findById(id)
                 .map(apiRouter -> {
                     if (StringUtils.isNotBlank(request.getDesc())) {
                         apiRouter.setDesc(request.getDesc());
                     }
                     if (StringUtils.isNotBlank(request.getPath())) {
                         apiRouter.setPath(request.getPath());
                     }
                     if (StringUtils.isNotBlank(request.getServerId())) {
                         apiRouter.setServerId(request.getServerId());
                     }

                     apiRouter.setBefore(request.getBeforeTime());
                     apiRouter.setAfter(request.getAfterTime());
                     if (request.getStatus() != null) {
                         apiRouter.setStatus(request.getStatus());
                     }
                     return apiRouter;
                 })
                 .map(apiRouterRepository::save).get();
    }

    @CacheEvict(allEntries = true)
    public void deleteRouter(Integer id) {
        apiRouterRepository
                .findById(id)
                .ifPresent(apiRouterRepository::delete);
    }
}
