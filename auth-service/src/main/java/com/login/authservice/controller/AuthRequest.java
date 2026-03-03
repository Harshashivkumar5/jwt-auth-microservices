package com.login.authservice.controller;

/**
 * DTO representing authentication request payload.
 */
public class AuthRequest {
    private String email;
    private String password;

    public AuthRequest() {
        // default constructor for deserialization
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}