package com.evolutivelabs.app.counter.database.mysql.repository;

import com.evolutivelabs.app.counter.database.mysql.entity.ApiRole;
import org.springframework.data.repository.CrudRepository;

public interface ApiRoleRepository extends CrudRepository<ApiRole, String> {
}
