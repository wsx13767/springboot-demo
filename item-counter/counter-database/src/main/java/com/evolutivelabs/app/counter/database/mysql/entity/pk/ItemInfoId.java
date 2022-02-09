package com.evolutivelabs.app.counter.database.mysql.entity.pk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ItemInfoId implements Serializable {
    @Column(name = "sku_prefix")
    private String skuPrefix;
    @Column(name = "sku_postfix")
    private String skuPostfix;
}
