package com.evolutivelabs.app.counter.database.mysql.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "box_info")
public class BoxInfo {
    @Id
    private String carton;
    @Column
    private BigDecimal qty;
    @Column(name = "len")
    private Integer length;
    @Column
    private Integer width;
    @Column
    private Integer height;
    @Column
    private String unit;
}
