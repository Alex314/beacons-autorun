package com.kpi.beaconsapp.model;

import java.util.Objects;

public class Beacon {
    private long id;
    private String name;
    private String description;
    private String code;

    public Beacon(long id, String name, String description, String code) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.code = code;
    }

    public Beacon(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Beacon beacon = (Beacon) o;
        return Objects.equals(code, beacon.code);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {

        return Objects.hash(code);
    }
}
