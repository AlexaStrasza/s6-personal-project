package com.alexstrasza.currency.entity;

import javax.persistence.*;

@Entity
@Table(name= "users")
public class UsersEntity
{
    public UsersEntity()
    {
    }

    public UsersEntity(String username) {
        this.username = username;
    }

    @Id
    @Column(columnDefinition = "VARCHAR(24)")
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
