package com.evolutivelabs.app.counter.database.mysql.repository;

import com.evolutivelabs.app.counter.database.mysql.entity.ApiRoleUser;
import com.evolutivelabs.app.counter.database.mysql.entity.pk.ApiRoleUserId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ApiRoleUserRepository extends CrudRepository<ApiRoleUser, ApiRoleUserId> {
    @Query("from ApiRoleUser a where a.account = ?1")
    List<ApiRoleUser> findByAccount(String account);
}
