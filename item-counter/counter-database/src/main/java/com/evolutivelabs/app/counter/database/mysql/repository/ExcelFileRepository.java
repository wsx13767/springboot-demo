package com.evolutivelabs.app.counter.database.mysql.repository;

import com.evolutivelabs.app.counter.database.mysql.entity.ExcelFile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ExcelFileRepository extends CrudRepository<ExcelFile, String> {
    @Query("select e from ExcelFile e where e.filePath = ?1")
    Optional<ExcelFile> findByFilePath(String filePath);

    @Query("select e from ExcelFile e where e.id = ?1 and e.done = ?2")
    Optional<ExcelFile> findByIdAndDone(String id, Boolean done);

    @Query("from ExcelFile e where e.done = ?1 and e.error = ?2")
    List<ExcelFile> findAllByDoneAndError(Boolean done, Boolean error);
}
