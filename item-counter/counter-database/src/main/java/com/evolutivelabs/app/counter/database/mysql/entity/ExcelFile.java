package com.evolutivelabs.app.counter.database.mysql.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "excel_file")
public class ExcelFile {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    private String id;
    @Column
    private String source;
    @Column
    private String filePath;
    @Column
    private String fileName;
    @Column
    private Boolean error = false;
    @Column
    private Boolean done = false;
    @Column
    private Long size;
    @Column
    private String msg;
    @Column(name = "modify_time")
    private LocalDateTime modifyTime;
    @CreationTimestamp
    @Column(name = "create_time")
    private LocalDateTime createTime;

}
