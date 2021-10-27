package com.evolutivelabs.order.database.repository;

import com.evolutivelabs.order.database.entity.Box;
import com.evolutivelabs.order.database.entity.id.BoxId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BoxRepository extends CrudRepository<Box, BoxId> {

    @Query("select b from Box b where b.sno = ?1")
    public List<Box> queryBySno(String sno);
}
