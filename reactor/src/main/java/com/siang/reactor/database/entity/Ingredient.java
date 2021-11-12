package com.siang.reactor.database.entity;

public class Ingredient {
    private String name;
    private String descript;
    private Type type;

    public Ingredient(String name, String descript, Type type) {
        this.name = name;
        this.descript = descript;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type {
        WRAP(1L, "wrap"),
        PROTEIN(2L, "protein");

        private Long id;
        private String des;

        Type(Long id, String des) {
            this.id = id;
            this.des = des;
        }

        public Long getId() {
            return id;
        }

        public String getDes() {
            return des;
        }
    }
}
