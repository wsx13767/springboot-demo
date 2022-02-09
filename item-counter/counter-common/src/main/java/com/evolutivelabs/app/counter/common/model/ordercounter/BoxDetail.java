package com.evolutivelabs.app.counter.common.model.ordercounter;

import com.evolutivelabs.app.counter.database.mysql.entity.ItemLog;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.*;
import java.util.stream.Collectors;

@ToString
@Getter
@ApiModel("裝箱資訊")
public class BoxDetail {
    @ApiModelProperty("裝箱序號")
    private String boxId;
    @ApiModelProperty("總數量")
    private Long total = 0L;
    @ApiModelProperty("產品明細")
    private List<Item> details;

    public BoxDetail() {}

    private BoxDetail(Builder builder) {
        this.boxId = builder.boxId;
        this.details = new ArrayList<>();
        for (Map.Entry<String, List<Log>> entry : builder.items.entrySet()) {
            Item item = new Item();
            item.sku = entry.getKey();
            item.logs = new ArrayList<>();
            for (Log log : entry.getValue()) {
                item.total += log.count;
                this.total += log.count;
                item.logs.add(log);
            }
            details.add(item);
        }
    }

    public List<Item> getDetails() {
        return Collections.unmodifiableList(details);
    }

    public static BoxDetail sum(BoxDetail v1, BoxDetail v2) {
        if (v1 == null) {
            return v2;
        }
        if (v2 == null) {
            return v1;
        }

        Builder builder = newBuilder(v1);
        builder.addItem(v2.getDetails());
        return builder.build();
    }

    public static Builder newBuilder(BoxDetail boxDetail) {
        Builder builder = newBuilder(boxDetail.boxId);
        builder.addItem(boxDetail.details);
        return builder;
    }

    public static Builder newBuilder(String boxId) {
        return new Builder(boxId);
    }

    public static class Builder {
        private String boxId;
        private Map<String, List<Log>> items = new HashMap<>();

        private Builder() {}

        private Builder(String boxId) {
            this.boxId = boxId;
        }

        private Builder(Builder builder) {
            this.boxId = builder.boxId;
        }

        private Builder addItem(List<Item> items) {
            for (Item item : items) {
                String sku = item.sku;
                List<Log> logs = this.items.get(sku);
                if (logs == null) {
                    logs = new ArrayList<>();
                    this.items.put(sku, logs);
                }
                logs.addAll(item.logs);
            }
            return this;
        }

        public Builder addItem(InBoxItems inBoxItems) {
            if (!this.boxId.equals(inBoxItems.getBoxId()))
                return this;

            String sku = inBoxItems.getBarcode();
            List<Log> logs = items.get(sku);
            if (logs == null) {
                logs = new ArrayList<>();
                items.put(sku, logs);
            }
            logs.add(new Log(inBoxItems.getTimestamp(), inBoxItems.getMultiple()));
            return this;
        }

        public Builder addItem(ItemLog itemEntity) {
            if (!this.boxId.equals(itemEntity.getBoxId()))
                return this;

            String sku = itemEntity.getBarcode();
            List<Log> logs = items.get(sku);
            if (logs == null) {
                logs = new ArrayList<>();
                items.put(sku, logs);
            }
            logs.add(new Log(itemEntity.getLogtime(), itemEntity.getMultiple()));
            return this;
        }

        public BoxDetail build() {
            return new BoxDetail(this);
        }

        public BoxDetail buildDistinct() {
            Map<String, List<Log>> newItems = new HashMap<>();
            for (Map.Entry<String, List<Log>> entry : this.items.entrySet()) {
                newItems.put(entry.getKey(), entry.getValue().stream().distinct().collect(Collectors.toList()));
            }
            Builder builder = new Builder();
            builder.boxId = this.boxId;
            builder.items = newItems;
            return new BoxDetail(builder);
        }
    }
}
