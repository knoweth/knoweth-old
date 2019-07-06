package com.github.knoweth.server.auth;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

public class UserUtils {
    public static User getUser() {
        return (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
