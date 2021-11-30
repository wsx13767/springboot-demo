package com.siang.security.server.database.repository;

import com.siang.security.server.database.entity.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Integer> {
}
