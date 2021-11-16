package com.siang.server.gateway.model.apiRoute;

import javax.persistence.Column;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class ApiRouteRequest {
    private String serverId;
    private String path;
    @Pattern(regexp = "[0-9]{4}-[0-9]{2}-[0-9]{2} [0-2][0-9]:[0-6][0-9]:[0-6][0-9]")
    private String before;
    @Pattern(regexp = "[0-9]{4}-[0-9]{2}-[0-9]{2} [0-2][0-9]:[0-6][0-9]:[0-6][0-9]")
    private String after;
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

    public LocalDateTime getBeforeTime() {
        return Optional.ofNullable(before).map(
                obj -> LocalDateTime.parse(obj, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        ).orElse(null);
    }

    public String getBefore() {
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }

    public LocalDateTime getAfterTime() {
        return Optional.ofNullable(after).map(
                obj -> LocalDateTime.parse(obj, DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"))
        ).orElse(null);
    }

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
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
}
