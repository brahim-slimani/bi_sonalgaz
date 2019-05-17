package com.slimani.bi_sonalgaz.adhoc.itemsParam;

public class AxeMeasure {
    private String name;
    private String role;

    public AxeMeasure(String name, String role) {
        this.name = name;
        this.role = role;
    }

    public AxeMeasure() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
