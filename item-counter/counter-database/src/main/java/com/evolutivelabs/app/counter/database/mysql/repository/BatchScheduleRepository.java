package com.evolutivelabs.app.counter.database.mysql.repository;

import com.evolutivelabs.app.counter.database.mysql.entity.BatchSchedule;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface BatchScheduleRepository extends CrudRepository<BatchSchedule, String> {

    @Query("from BatchSchedule b where b.taskId = ?1 and b.status = ?2 and b.system = ?3")
    Optional<BatchSchedule> findBy(String taskId, Boolean status, String system);

    @Query("from BatchSchedule b where b.status = ?1 and b.system = ?2")
    List<BatchSchedule> findAllByStatusAndSystem(Boolean status, String system);

    @Query("from BatchSchedule b where b.system = ?1")
    List<BatchSchedule> findBySystem(String system);
}
