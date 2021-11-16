package com.siang.server.gateway.model.apiRoute;

import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

public class ApiRouteResponse {
    private String serverId;
    private String path;
    private LocalDateTime before;
    private LocalDateTime after;
    private String desc;
    private Boolean status;

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ApiRouteResponse{" +
                "serverId='" + serverId + '\'' +
                ", path='" + path + '\'' +
                ", before=" + before +
                ", after=" + after +
                ", desc='" + desc + '\'' +
                ", status=" + status +
                '}';
    }
}
