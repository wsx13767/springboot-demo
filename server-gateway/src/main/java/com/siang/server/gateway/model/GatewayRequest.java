package com.siang.server.gateway.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GatewayRequest {
    private String apiId;
    private String uri;
    private String hostName;
    private String path;
    @Pattern(regexp = "[0-9]{4}-[0-9]{2}-[0-9]{2} [0-2][0-9]:[0-6][0-9]:[0-6][0-9]")
    private String before;
    @Pattern(regexp = "[0-9]{4}-[0-9]{2}-[0-9]{2} [0-2][0-9]:[0-6][0-9]:[0-6][0-9]")
    private String after;
    private Boolean status;

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

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public LocalDateTime getBeforeTime() {
        return LocalDateTime.parse(before, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String getBefore() {
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }

    public LocalDateTime getAfterTime() {
        return LocalDateTime.parse(after, DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
    }

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
    }
}
