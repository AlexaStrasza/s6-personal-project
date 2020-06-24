package com.alexstrasza.auctioning.models;

public class UserModel
{
    private int id;
    private String username;
    private String email;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserModel(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public UserModel(int id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public UserModel() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
