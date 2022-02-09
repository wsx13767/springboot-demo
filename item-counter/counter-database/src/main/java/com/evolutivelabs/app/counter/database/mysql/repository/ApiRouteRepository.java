package com.evolutivelabs.app.counter.database.mysql.repository;

import com.evolutivelabs.app.counter.database.mysql.entity.ApiRoute;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ApiRouteRepository extends CrudRepository<ApiRoute, String> {

    @Query("from ApiRoute a where a.serverId = ?1 and a.status = ?2")
    List<ApiRoute> findByServerIdAndStatus(String serverId, Boolean status);

    @Query("from ApiRoute a where a.status = ?1 and a.docs = ?2")
    List<ApiRoute> findDocsByStatus(Boolean status, Boolean docs);
}
