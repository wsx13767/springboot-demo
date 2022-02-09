package com.evolutivelabs.app.counter.database.mysql.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "batch_schedule")
public class BatchSchedule {
    @Id
    @Column(name = "task_id")
    private String taskId;

    @Column
    private String name;

    @Column
    private String description;

    @Column(name = "cron_expression")
    private String cronExpression;

    @Column
    private Boolean status;

    @Column(name = "class_name")
    private String className;

    @Column
    private String system;
}
