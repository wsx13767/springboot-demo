package com.siang.server.gateway.database.repository;

import com.siang.server.gateway.database.entity.ApiRoute;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ApiRouterRepository extends CrudRepository<ApiRoute, Integer> {
    @Query("select a from ApiRoute a where a.serverId = ?1")
    List<ApiRoute> findByServerId(String serverId);
}
