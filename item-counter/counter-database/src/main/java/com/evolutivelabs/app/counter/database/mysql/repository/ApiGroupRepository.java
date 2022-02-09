package com.evolutivelabs.app.counter.database.mysql.repository;

import com.evolutivelabs.app.counter.database.mysql.entity.ApiGroup;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ApiGroupRepository extends CrudRepository<ApiGroup, String> {

    @Query("from ApiGroup a where a.status = ?1")
    List<ApiGroup> findByStatus(Boolean status);
}
