package com.evolutivelabs.order.database.entity.id;

import java.io.Serializable;
import java.util.Objects;

public class BoxId implements Serializable {
    private String sno;
    private String id;

    public BoxId() {}

    public BoxId(String sno, String id) {
        this.sno = sno;
        this.id = id;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoxId boxId = (BoxId) o;
        return Objects.equals(sno, boxId.sno) && Objects.equals(id, boxId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sno, id);
    }
}
