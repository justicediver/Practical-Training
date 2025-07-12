package com.neu.project.entity;


public class User {
    private int id;
    private String pw;
    private String username;

    public User(int id, String pw, String username) {
        this.id = id;
        this.pw = pw;
        this.username = username;
    }

    public User() {}
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
