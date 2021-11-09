package com.siang.server.gateway.model;

import javax.validation.constraints.NotBlank;

public class GatewayRequest {
    @NotBlank
    private String apiId;
    @NotBlank
    private String uri;
    @NotBlank
    private String hostName;
    @NotBlank
    private String path;

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
