package com.evolutivelabs.app.counter.database.mysql.entity;

import com.evolutivelabs.app.counter.database.mysql.entity.pk.BoxOfExcelId;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "box_of_excel")
@IdClass(BoxOfExcelId.class)
public class BoxOfExcel {
    @Id
    @Column(name = "excel_id")
    private String excelId;

    @Id
    @Column(name = "box_series")
    private Integer boxSeries;

    @Column(name = "box_carton")
    private String boxCarton;
}
