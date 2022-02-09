package com.evolutivelabs.app.counter.database.mysql.repository;

import com.evolutivelabs.app.counter.database.mysql.entity.BoxInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BoxInfoRepository extends CrudRepository<BoxInfo, String> {

    @Query("from BoxInfo b order by b.qty desc")
    List<BoxInfo> findAllOrderByQty();
}
