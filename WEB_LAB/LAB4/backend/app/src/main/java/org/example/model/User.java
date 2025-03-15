package com.example.model;

import jakarta.persistence.*;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Entity
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    public User() {}


    public User(String username, String password) {
        this.username = username;
        this.password = password; 
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }


    public static String hashPassword(String raw) {
        return BCrypt.hashpw(raw, BCrypt.gensalt());
    }
}