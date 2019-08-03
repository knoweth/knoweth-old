package com.github.knoweth.client.services;

public class LoginBody {
    public LoginBody(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public String username;
    public String password;
}
