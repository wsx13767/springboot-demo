package com.evolutivelabs.app.counter.database.mysql.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "api_route")
public class ApiRoute {
    @Id
    private String id;
    @Column(name = "server_id")
    private String serverId;
    @Column
    private String name;
    @Column
    private String method;
    @Column
    private String uri;
    @Column
    private LocalDateTime before;
    @Column
    private LocalDateTime after;
    @Column
    private Boolean status;
    @Column
    private Boolean docs;
    @Column
    private String description;

}
