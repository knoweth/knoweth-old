package com.github.knoweth.server.auth;

import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtils {
    public static User getUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
