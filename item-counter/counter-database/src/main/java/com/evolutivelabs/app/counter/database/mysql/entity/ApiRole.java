package com.evolutivelabs.app.counter.database.mysql.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "api_role")
public class ApiRole {
    @Id
    private String id;
    @Column
    private String name;
    @Column
    private String description;
}
