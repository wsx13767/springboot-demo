package com.evolutivelabs.app.counter.database.mysql.repository;

import com.evolutivelabs.app.counter.database.mysql.entity.ItemsOfBox;
import com.evolutivelabs.app.counter.database.mysql.entity.pk.ItemsOfBoxId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ItemsOfBoxRepository extends CrudRepository<ItemsOfBox, ItemsOfBoxId> {
    @Query("from ItemsOfBox i where i.excelId = ?1")
    List<ItemsOfBox> findByExcelId(String excelId);
}
