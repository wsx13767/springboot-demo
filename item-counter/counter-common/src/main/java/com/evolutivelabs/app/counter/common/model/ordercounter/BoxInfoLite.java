package com.evolutivelabs.app.counter.common.model.ordercounter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel("箱內簡易資訊")
public class BoxInfoLite {
    @ApiModelProperty("裝箱序號")
    private String boxId;
    @ApiModelProperty("總數量")
    private Long total;
    @ApiModelProperty("商品資訊")
    private List<ItemInfoLite> items;

    public String getBoxId() {
        return boxId;
    }

    public void setBoxId(String boxId) {
        this.boxId = boxId;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<ItemInfoLite> getItems() {
        return items;
    }

    public void setItems(List<ItemInfoLite> items) {
        this.items = items;
    }
}
