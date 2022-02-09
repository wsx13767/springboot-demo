package com.evolutivelabs.app.counter.common.model.ordercounter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("商品簡易資訊")
public class ItemInfoLite {
    @ApiModelProperty("商品序號")
    private String sku;
    @ApiModelProperty("總數量")
    private Long total;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
