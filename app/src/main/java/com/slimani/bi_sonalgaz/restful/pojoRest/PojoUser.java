package com.slimani.bi_sonalgaz.restful.pojoRest;

public class PojoUser {

    private String username;
    private String password;
    private String role;
    private String email;

    public PojoUser(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public PojoUser(String username, String password, String role, String email) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
    }

    public PojoUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public PojoUser() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
