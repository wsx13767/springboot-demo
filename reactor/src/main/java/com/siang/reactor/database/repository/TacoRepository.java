package com.siang.reactor.database.repository;

import com.siang.reactor.database.entity.Taco;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface TacoRepository extends ReactiveCrudRepository<Taco, Long> {
}
