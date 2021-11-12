package com.siang.server.gateway.database.entity;


import javax.persistence.*;

@Entity
@Table(name = "api_router")
public class ApiRouter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String apiId;
    @Column
    private String uri;
    @Column(name = "host_name")
    private String hostName;
    @Column
    private String path;

    public ApiRouter() {
    }

    public ApiRouter(String apiId, String uri, String hostName, String path) {
        this.apiId = apiId;
        this.uri = uri;
        this.hostName = hostName;
        this.path = path;
    }

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

    @Override
    public String toString() {
        return "ApiRouter{" +
                "id=" + id +
                ", apiId='" + apiId + '\'' +
                ", uri='" + uri + '\'' +
                ", hostName='" + hostName + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
