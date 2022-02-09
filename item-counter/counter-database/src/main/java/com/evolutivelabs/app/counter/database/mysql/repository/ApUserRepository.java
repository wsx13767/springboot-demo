package com.evolutivelabs.app.counter.database.mysql.repository;

import com.evolutivelabs.app.counter.database.mysql.entity.ApUser;
import org.springframework.data.repository.CrudRepository;

public interface ApUserRepository extends CrudRepository<ApUser, String> {
}
