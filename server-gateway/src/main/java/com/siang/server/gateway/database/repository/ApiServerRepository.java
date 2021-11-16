package com.siang.server.gateway.database.repository;

import com.siang.server.gateway.database.entity.ApiServer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ApiServerRepository extends CrudRepository<ApiServer, String> {
    @Query("select a from ApiServer a where a.groupId = ?1")
    public List<ApiServer> findByGroupId(String groupId);
}
