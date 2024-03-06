package com.depot.ims.accessingdatajpa;

import jakarta.persistence.*;

@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_users")
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    protected User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return String.format(
                "User[id=%d, username='%s', password='%s']",
                id, username, password);
    }

    public int getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
