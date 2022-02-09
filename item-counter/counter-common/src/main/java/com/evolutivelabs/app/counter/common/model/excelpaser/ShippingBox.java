package com.evolutivelabs.app.counter.common.model.excelpaser;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class ShippingBox {
    private String boxCarton;
    private BigDecimal boxQty;
    private boolean isOK;
    private List<ItemBundle> itemBundles = new ArrayList<>();

    /**
     * 取得總體積數
     * @return
     */
    public BigDecimal getNowQty() {
        return this.itemBundles
                .stream()
                .map(itemBundle -> itemBundle.getTotalVolume())
                .reduce((v1, v2) -> v1.add(v2)).get();
    }

    public boolean add(ItemBundle... itemBundle) {
        return itemBundles.addAll(Arrays.asList(itemBundle));
    }
}
