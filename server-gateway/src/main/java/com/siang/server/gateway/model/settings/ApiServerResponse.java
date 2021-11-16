package com.siang.server.gateway.model.settings;

import com.siang.server.gateway.database.entity.ApiRoute;
import com.siang.server.gateway.database.entity.ApiServer;

import java.util.List;

public class ApiServerResponse extends ApiServer {
    private List<ApiRoute> apiRoutes;

    public ApiServerResponse() {
    }

    public ApiServerResponse(ApiServer apiServer) {
        super.setId(apiServer.getId());
        super.setName(apiServer.getName());
        super.setUri(apiServer.getUri());
        super.setGroupId(apiServer.getGroupId());
        super.setDesc(apiServer.getDesc());
        super.setStatus(apiServer.getStatus());
    }

    public List<ApiRoute> getApiRoutes() {
        return apiRoutes;
    }

    public void setApiRoutes(List<ApiRoute> apiRoutes) {
        this.apiRoutes = apiRoutes;
    }
}
