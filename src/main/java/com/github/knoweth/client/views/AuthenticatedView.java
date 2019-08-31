package com.github.knoweth.client.views;

import com.github.knoweth.client.services.Services;
import org.teavm.flavour.rest.HttpStatusException;
import org.teavm.flavour.widgets.BackgroundWorker;
import org.teavm.jso.browser.Location;

import javax.servlet.http.HttpServletResponse;

/**
 * Abstract view class that ensures the user is logged in when viewing it.
 *
 * Otherwise, it will redirect the user to the login page.
 */
public abstract class AuthenticatedView {
    public AuthenticatedView() {
        new BackgroundWorker().run(() -> {
            try {
                // Query to see if logged in (this will return 403 otherwise)
                Services.USER.status();
            } catch (HttpStatusException e) {
                if (e.getStatus() == HttpServletResponse.SC_UNAUTHORIZED) {
                    Location.current().setHash("/login");
                }
            }
        });
    }
}
