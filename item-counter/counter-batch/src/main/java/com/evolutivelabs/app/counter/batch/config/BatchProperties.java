package com.evolutivelabs.app.counter.batch.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class BatchProperties {
    @Value("${evolutivelabs.batch.excel.root.path}")
    private String EXCEL_ROOT_PATH;
}
