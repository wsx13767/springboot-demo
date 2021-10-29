package com.evolutivelabs.ordermanager.database.repository;

import com.evolutivelabs.ordermanager.database.entity.Menu;
import org.springframework.data.repository.CrudRepository;

public interface MenuRepository extends CrudRepository<Menu, String> {
}
