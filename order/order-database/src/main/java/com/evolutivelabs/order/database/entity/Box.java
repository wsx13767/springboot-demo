package com.evolutivelabs.order.database.entity;

import com.evolutivelabs.order.database.entity.id.BoxId;

import javax.persistence.*;

@Entity
@Table(name = "box")
@IdClass(BoxId.class)
public class Box {
    @Id
    private String sno;

    @Id
    private String id;

    @Column
    private String name;

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
