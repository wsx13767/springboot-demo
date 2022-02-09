package com.evolutivelabs.app.counter.database.mysql.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "counting_item_kafka_log")
public class ItemKafkaLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sno;

    @Column(name = "box_id")
    private String boxId;

    @Column
    private String sku;

    @Column
    private Long num;

    @Column
    private Boolean error;

    @Column
    private Long logtime;

    @CreationTimestamp
    @Column(name = "create_time")
    private LocalDateTime createTime;

    public Long getSno() {
        return sno;
    }

    public void setSno(Long sno) {
        this.sno = sno;
    }

    public String getBoxId() {
        return boxId;
    }

    public void setBoxId(String boxId) {
        this.boxId = boxId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public Long getLogtime() {
        return logtime;
    }

    public void setLogtime(Long logtime) {
        this.logtime = logtime;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }


}
