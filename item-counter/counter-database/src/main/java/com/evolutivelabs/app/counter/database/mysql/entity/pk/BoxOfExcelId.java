package com.evolutivelabs.app.counter.database.mysql.entity.pk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoxOfExcelId implements Serializable {
    private String excelId;
    private Integer boxSeries;
}
