package com.evolutivelabs.app.counter.database.mysql.repository;

import com.evolutivelabs.app.counter.database.mysql.entity.PackingConfig;
import org.springframework.data.repository.CrudRepository;

public interface PackingConfigRepository extends CrudRepository<PackingConfig, String> {
}
