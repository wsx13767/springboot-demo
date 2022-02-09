package com.evolutivelabs.app.counter.database.mysql.entity.pk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemsOfBoxId implements Serializable {
    private String sku;
    private String excelId;
    private Integer boxSeries;
}
