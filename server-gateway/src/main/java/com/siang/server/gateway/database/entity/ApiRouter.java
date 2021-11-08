package com.siang.server.gateway.database.entity;


import org.springframework.data.annotation.Id;

public class ApiRouter {
    @Id
    private Integer id;

    private String uri;

    private String hostName;

    private String apiId;

    private String path;

//    public ApiRouter() {
//    }

    public ApiRouter(String uri, String hostName, String apiId, String path) {
        this.uri = uri;
        this.hostName = hostName;
        this.apiId = apiId;
        this.path = path;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "ApiRouter{" +
                "id=" + id +
                ", uri='" + uri + '\'' +
                ", hostName='" + hostName + '\'' +
                ", apiId='" + apiId + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
