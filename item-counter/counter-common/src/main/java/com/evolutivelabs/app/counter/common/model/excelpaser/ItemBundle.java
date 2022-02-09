package com.evolutivelabs.app.counter.common.model.excelpaser;

import com.evolutivelabs.app.counter.database.mysql.entity.ItemInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ItemBundle {
    private String sku;
    private String fnsku;
    private String title;
    private BigDecimal num;
    private Integer order;
    private ItemInfo itemInfo;
    private boolean isMax;

    public ItemBundle(ItemBundle row) {
        this.sku = row.sku;
        this.fnsku = row.fnsku;
        this.title = row.title;
        this.num = row.getNum();
        this.order = row.order;
        this.itemInfo = row.itemInfo;
    }

    public BigDecimal getTotalVolume() {
        return this.getNum().multiply(this.itemInfo.getVolume());
    }
}
