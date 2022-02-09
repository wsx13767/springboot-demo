package com.evolutivelabs.app.counter.common.model.ordercounter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Collections;
import java.util.List;

@ApiModel("產品資訊")
public class Item {
    @ApiModelProperty("產品代碼")
    String sku;
    @ApiModelProperty("總數量")
    Long total = 0L;
    @ApiModelProperty("紀錄")
    List<Log> logs;

    public Item() {}

    public String getSku() {
        return sku;
    }

    public Long getTotal() {
        return total;
    }

    public List<Log> getLogs() {
        return Collections.unmodifiableList(logs);
    }

    @Override
    public String toString() {
        return "Item{" +
                "sku='" + sku + '\'' +
                ", total=" + total +
                ", logs=" + logs +
                '}';
    }
}
