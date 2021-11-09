package com.siang.server.gateway.model;

public class GatewayResponse {
    private Integer id;
    private String apiId;
    private String uri;
    private String path;
    private String hostName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    @Override
    public String toString() {
        return "GatewayResponse{" +
                "id=" + id +
                ", apiId='" + apiId + '\'' +
                ", uri='" + uri + '\'' +
                ", path='" + path + '\'' +
                ", hostName='" + hostName + '\'' +
                '}';
    }
}
