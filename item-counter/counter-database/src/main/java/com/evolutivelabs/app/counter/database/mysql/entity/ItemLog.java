package com.evolutivelabs.app.counter.database.mysql.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "counting_item_log")
public class ItemLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sno;

    @Column(name = "shippment_id")
    private String shippmentId;

    @Column(name = "box_id")
    private String boxId;

    @Column
    private Long logtime;

    @Column
    private String barcode;

    @Column
    private String source;

    @Column
    private Boolean error;

    @Column
    private String status;

    @Column
    private Long multiple;

    @CreationTimestamp
    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column
    private String ip;
}
