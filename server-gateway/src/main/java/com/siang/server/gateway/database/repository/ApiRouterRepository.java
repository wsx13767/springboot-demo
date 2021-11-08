package com.siang.server.gateway.database.repository;

import com.siang.server.gateway.database.entity.ApiRouter;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ApiRouterRepository extends ReactiveCrudRepository<ApiRouter, Integer> {
}
