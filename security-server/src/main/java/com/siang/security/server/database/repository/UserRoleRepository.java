package com.siang.security.server.database.repository;

import com.siang.security.server.database.entity.UserRole;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRoleRepository extends CrudRepository<UserRole, Integer> {
    @Query("select u from UserRole u where u.uid = ?1")
    List<UserRole> findByUid(String uid);
}
