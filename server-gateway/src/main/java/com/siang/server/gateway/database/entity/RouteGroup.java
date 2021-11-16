package com.siang.server.gateway.database.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "route_group")
public class RouteGroup {
    @Id
    private String id;
    @Column(name = "description")
    private String desc;
    @Column
    private Boolean status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        return "RouteGroup{" +
                "id='" + id + '\'' +
                ", desc='" + desc + '\'' +
                ", status=" + status +
                '}';
    }
}
