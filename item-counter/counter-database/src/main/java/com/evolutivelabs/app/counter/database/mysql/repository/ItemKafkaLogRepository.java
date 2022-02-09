package com.evolutivelabs.app.counter.database.mysql.repository;

import com.evolutivelabs.app.counter.database.mysql.entity.ItemKafkaLog;
import org.springframework.data.repository.CrudRepository;

public interface ItemKafkaLogRepository extends CrudRepository<ItemKafkaLog, Long> {
}
