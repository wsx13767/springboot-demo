package com.siang.server.gateway.model.settings;

import com.siang.server.gateway.database.entity.RouteGroup;

import java.util.List;

public class RouteGroupResponse extends RouteGroup {
    private List<ApiServerResponse> apiServers;


    public RouteGroupResponse() {
    }

    public RouteGroupResponse(RouteGroup routeGroup) {
        super.setId(routeGroup.getId());
        super.setDesc(routeGroup.getDesc());
        super.setStatus(routeGroup.getStatus());
    }

    public List<ApiServerResponse> getApiServers() {
        return apiServers;
    }

    public void setApiServers(List<ApiServerResponse> apiServers) {
        this.apiServers = apiServers;
    }
}
