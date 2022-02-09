package com.evolutivelabs.app.counter.common.model.excelpaser;

import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ExcelResult {
    private LocalDateTime lastModified;
    private String id;
    private String path;
    private List<ExcelSheet> sheets = new ArrayList<>();

    public void add(ExcelSheet sheet) {
        this.sheets.add(sheet);
    }
}
