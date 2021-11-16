package com.siang.security.server.database.repository;

import com.siang.security.server.database.entity.APUser;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<APUser, String> {
}
