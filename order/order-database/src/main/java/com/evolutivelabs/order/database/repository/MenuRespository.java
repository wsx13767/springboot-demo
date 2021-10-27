package com.evolutivelabs.order.database.repository;

import com.evolutivelabs.order.database.entity.Menu;
import org.springframework.data.repository.CrudRepository;

public interface MenuRespository extends CrudRepository<Menu, String> {
}
