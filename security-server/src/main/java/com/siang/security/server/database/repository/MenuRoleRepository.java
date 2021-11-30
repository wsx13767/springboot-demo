package com.siang.security.server.database.repository;

import com.siang.security.server.database.entity.MenuRole;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MenuRoleRepository extends CrudRepository<MenuRole, Integer> {
    @Query("select m from MenuRole m where m.mid = ?1")
    List<MenuRole> findByMid(Integer mid);
}
