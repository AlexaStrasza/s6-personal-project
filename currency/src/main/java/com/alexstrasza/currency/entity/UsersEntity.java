package com.alexstrasza.currency.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Set;

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
