package com.siang.server.gateway.database.repository;

import com.siang.server.gateway.database.entity.ApiRoute;
import org.springframework.data.repository.CrudRepository;

public interface ApiRouterRepository extends CrudRepository<ApiRoute, Integer> {
}
