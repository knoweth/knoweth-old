package com.github.knoweth.common.data;

public class Account {
    private String username;
    private String email;

    public Account(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }
}
