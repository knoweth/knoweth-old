package com.github.knoweth.server.auth;

import org.springframework.security.core.context.SecurityContextHolder;

/**
 * User utility functions.
 */
public class UserUtils {
    /**
     * @return the current logged-in user
     */
    public static User getUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
