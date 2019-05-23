package com.slimani.bi_sonalgaz.restful.pojoRest;

public class PojoReport {
    private String title;
    private String context;
    private String type;
    private String username;

    public PojoReport(String title, String context, String type, String username) {
        this.title = title;
        this.context = context;
        this.type = type;
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
