package com.siang.security.server.database.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "menu_role")
public class MenuRole implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private Integer mid;
    @Column
    private Integer rid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    @Override
    public String toString() {
        return "MenuRole{" +
                "id=" + id +
                ", mid=" + mid +
                ", rid=" + rid +
                '}';
    }
}
