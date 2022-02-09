package com.evolutivelabs.app.counter.database.mysql.repository;

import com.evolutivelabs.app.counter.database.mysql.entity.ItemInfo;
import com.evolutivelabs.app.counter.database.mysql.entity.pk.ItemInfoId;
import org.springframework.data.repository.CrudRepository;

public interface ItemInfoRepository extends CrudRepository<ItemInfo, ItemInfoId> {
}
