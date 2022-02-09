package com.evolutivelabs.app.counter.database.mysql.repository;

import com.evolutivelabs.app.counter.database.mysql.entity.BoxOfBusiness;
import com.evolutivelabs.app.counter.database.mysql.entity.pk.BusinessBoxId;
import org.springframework.data.repository.CrudRepository;

public interface BoxOfBusinessRepository extends CrudRepository<BoxOfBusiness, BusinessBoxId> {
}
