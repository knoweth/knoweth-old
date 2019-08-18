package com.github.knoweth.client.cache;

import com.github.knoweth.client.services.Services;
import com.github.knoweth.server.auth.User;
import org.springframework.lang.Nullable;
import org.teavm.flavour.rest.HttpStatusException;

import javax.servlet.http.HttpServletResponse;

/**
 * Caches server-side information for reuse from multiple components. Similar
 * to Redux, but without all the functional programming.
 */
public class DataCache implements UserCache {
    private boolean isUserLoaded;
    private User currentUser;

    @Override
    public User getCurrentUser() {
        if (!isUserLoaded()) {
            throw new IllegalStateException("Cannot get current user when not loaded");
        }
        return currentUser;
    }

    @Override
    public User loadCurrentUser() {
        try {
            currentUser = Services.USER.status();
        } catch (HttpStatusException e) {
            if (e.getStatus() == HttpServletResponse.SC_UNAUTHORIZED) {
                // Unauthorized = no user logged in, not considered an error
                // case
                currentUser = null;
            } else {
                throw e;
            }
        }
        isUserLoaded = true;
        return currentUser;
    }

    @Override
    public boolean isUserLoaded() {
        return isUserLoaded;
    }
}
