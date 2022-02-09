package com.evolutivelabs.app.counter.database.mysql.entity;

import com.evolutivelabs.app.counter.database.mysql.entity.pk.ItemsOfBoxId;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "items_of_box")
@IdClass(ItemsOfBoxId.class)
public class ItemsOfBox {
    @Id
    @Column
    private String sku;
    @Id
    @Column(name = "excel_id")
    private String excelId;
    @Id
    @Column(name = "box_series")
    private Integer boxSeries;
    @Column
    private String position;
    @Column(name = "fn_sku")
    private String fnSku;
    @Column
    private String ean;
    @Column
    private Integer num;
    @Column
    private BigDecimal weight;
    @Column
    private BigDecimal volume;
    @Column
    private String title;

}
