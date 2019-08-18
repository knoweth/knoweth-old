package com.github.knoweth.client.cache;

import com.github.knoweth.server.auth.User;

/**
 * Interface to encapsulate a cache for the current user state.
 */
public interface UserCache {
    User getCurrentUser();
    void loadCurrentUser();
    boolean isUserLoaded();
}
