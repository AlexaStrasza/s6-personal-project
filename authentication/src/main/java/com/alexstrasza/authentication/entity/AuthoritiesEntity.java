package com.alexstrasza.authentication.entity;

import com.alexstrasza.authentication.service.Authority;

import javax.persistence.*;

@Entity
@Table(name ="authorities")
public class AuthoritiesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UsersEntity user;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    public AuthoritiesEntity(UsersEntity user, Authority authority) {
        this.user = user;
        this.authority = authority;
    }

    public AuthoritiesEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Authority getAuthority() {
        return authority;
    }

    public void setAuthority(Authority authority) {
        this.authority = authority;
    }

    public UsersEntity getUser() {
        return user;
    }

    public void setUser(UsersEntity user) {
        this.user = user;
    }
}
