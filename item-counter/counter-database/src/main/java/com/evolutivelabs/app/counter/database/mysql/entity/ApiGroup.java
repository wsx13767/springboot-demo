package com.evolutivelabs.app.counter.database.mysql.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "api_group")
public class ApiGroup {
    @Id
    private String id;
    @Column
    private Boolean status;
    @Column
    private String description;
}
