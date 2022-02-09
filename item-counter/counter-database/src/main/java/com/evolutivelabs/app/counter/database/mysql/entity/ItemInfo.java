package com.evolutivelabs.app.counter.database.mysql.entity;

import com.evolutivelabs.app.counter.database.mysql.entity.pk.ItemInfoId;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;


@Data
@Entity
@Table(name = "item_info")
public class ItemInfo {
    @EmbeddedId
    private ItemInfoId itemInfoId;
    @Column
    private String category;
    @Column
    private BigDecimal volume;
    @Column
    private BigDecimal weight;
    @Column
    private Integer sequence;
}
