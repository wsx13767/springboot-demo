package com.evolutivelabs.app.counter.database.mysql.repository;

import com.evolutivelabs.app.counter.database.mysql.entity.BoxOfExcel;
import com.evolutivelabs.app.counter.database.mysql.entity.pk.BoxOfExcelId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BoxOfExcelRepository extends CrudRepository<BoxOfExcel, BoxOfExcelId> {
    @Query("from BoxOfExcel b where b.excelId = ?1")
    List<BoxOfExcel> findByExcelId(String excelId);
}
