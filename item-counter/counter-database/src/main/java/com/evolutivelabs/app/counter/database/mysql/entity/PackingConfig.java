package com.evolutivelabs.app.counter.database.mysql.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "packing_config")
public class PackingConfig {
    @Id
    private String id;
    @Column
    private String sku;
    @Column
    private String fnsku;
    @Column
    private String title;
    @Column
    private String num;
}
