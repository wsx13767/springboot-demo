package com.evolutivelabs.app.counter.database.mysql.repository;

import com.evolutivelabs.app.counter.database.mysql.entity.ApiServer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ApiServerRepository extends CrudRepository<ApiServer, String> {

    @Query("from ApiServer a where a.groupId = ?1 and a.status = ?2")
    List<ApiServer> findByGroupIdAndStatus(String groupId, Boolean status);
}
