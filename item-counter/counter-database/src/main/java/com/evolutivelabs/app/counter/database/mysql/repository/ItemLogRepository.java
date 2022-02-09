package com.evolutivelabs.app.counter.database.mysql.repository;

import com.evolutivelabs.app.counter.database.mysql.entity.ItemLog;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface ItemLogRepository extends CrudRepository<ItemLog, Long> {

    @Transactional
    @Modifying
    @Query("delete from ItemLog i where i.boxId = :boxId")
    Integer deleteByBoxId(@Param("boxId") String boxId);

    @Query("select i from ItemLog i where i.boxId = ?1")
    List<ItemLog> findByBoxId(String boxId);
}
