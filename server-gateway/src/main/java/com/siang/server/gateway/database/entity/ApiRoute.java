package com.siang.server.gateway.database.entity;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "api_route")
public class ApiRoute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "server_id")
    private String serverId;
    @Column
    private String path;
    @Column
    private LocalDateTime before;
    @Column
    private LocalDateTime after;
    @Column(name = "description")
    private String desc;
    @Column
    private Boolean status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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
        return "ApiRoute{" +
                "id=" + id +
                ", serverId='" + serverId + '\'' +
                ", path='" + path + '\'' +
                ", before=" + before +
                ", after=" + after +
                ", desc='" + desc + '\'' +
                ", status=" + status +
                '}';
    }
}
