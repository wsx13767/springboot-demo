package com.siang.server.gateway.database.entity;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "api_route")
public class ApiRoute {
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
    @Column
    private LocalDateTime before;
    @Column
    private LocalDateTime after;
    @Column
    private Boolean status;

    public ApiRoute() {
    }

    public ApiRoute(String apiId, String uri, String hostName, String path, LocalDateTime before, LocalDateTime after, Boolean status) {
        this.apiId = apiId;
        this.uri = uri;
        this.hostName = hostName;
        this.path = path;
        this.before = before;
        this.after = after;
        this.status = status;
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

    public LocalDateTime getBefore() {
        return before;
    }

    public void setBefore(LocalDateTime before) {
        this.before = before;
    }

    public LocalDateTime getAfter() {
        return after;
    }

    public void setAfter(LocalDateTime after) {
        this.after = after;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ApiRoute{" +
                "id=" + id +
                ", apiId='" + apiId + '\'' +
                ", uri='" + uri + '\'' +
                ", hostName='" + hostName + '\'' +
                ", path='" + path + '\'' +
                ", before=" + before +
                ", after=" + after +
                ", status=" + status +
                '}';
    }
}
