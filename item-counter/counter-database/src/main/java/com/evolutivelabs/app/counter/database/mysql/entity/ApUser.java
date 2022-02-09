package com.evolutivelabs.app.counter.database.mysql.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "ap_user")
public class ApUser {
    @Id
    private String account;

    @Column
    private String password;

    @Column
    private String email;

    @Column
    private String name;
}
