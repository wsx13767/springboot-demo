package com.evolutivelabs.app.counter.database.mysql.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "api_server")
public class ApiServer {
    @Id
    private String id;
    @Column(name = "group_id")
    private String groupId;
    @Column
    private String name;
    @Column
    private String host;
    @Column
    private Boolean status;
    @Column
    private String description;
}
