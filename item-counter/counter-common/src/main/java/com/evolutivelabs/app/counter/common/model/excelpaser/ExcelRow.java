package com.evolutivelabs.app.counter.common.model.excelpaser;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExcelRow {
    private String boxSizeSpec;
    private String boxSpec;
    private String box_number;
    private String box_spec;
    private String ean;
    private String fnsku;
    private String image = "http://i1.wp.com/inews.gtimg.com/newsapp_bt/0/1627177403/641";
    private Long maxQtyl;
    private Long maxVolumn;
    private Long netWeight;
    private String position;
    private Integer priority;
    private Integer qty;
    private Long qty_bumper;
    private Long qty_done = 0L;
    private Long qty_typed;
    private String sku;
    private String skuId;
    private Long summaryQty;
    private String title;
    private String upc;
    private BigDecimal volumn;
    private BigDecimal volumns;
    private BigDecimal weight;
    private BigDecimal weights;
}
