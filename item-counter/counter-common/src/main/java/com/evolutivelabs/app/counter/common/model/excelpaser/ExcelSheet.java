package com.evolutivelabs.app.counter.common.model.excelpaser;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ExcelSheet {
    private String box;
    private Boolean fulfilled;
    @JsonProperty("sheet_name")
    private String sheetName;
    private List<ExcelRow> rows = new ArrayList<>();

    public void add(ExcelRow row) {
        this.rows.add(row);
    }

}
