package com.alexstrasza.inventory.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
    @Column(columnDefinition = "VARCHAR(20)")
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
