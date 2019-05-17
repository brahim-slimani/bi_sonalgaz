package com.slimani.bi_sonalgaz.adhoc.itemsParam;

public class AxeDimension {
    private String name;
    private String role;

    public AxeDimension(String name, String role) {
        this.name = name;
        this.role = role;
    }

    public AxeDimension() {
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
