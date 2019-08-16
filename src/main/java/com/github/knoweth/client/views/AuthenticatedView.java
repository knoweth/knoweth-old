package com.github.knoweth.client.views;

import com.github.knoweth.client.services.Services;
import org.teavm.flavour.rest.HttpStatusException;
import org.teavm.flavour.widgets.BackgroundWorker;
import org.teavm.jso.browser.Location;

import javax.servlet.http.HttpServletResponse;

public abstract class AuthenticatedView {
    public AuthenticatedView() {
        new BackgroundWorker().run(() -> {
            try {
                Services.USER.welcome();
            } catch (HttpStatusException e) {
                if (e.getStatus() == HttpServletResponse.SC_UNAUTHORIZED) {
                    Location.current().setFullURL("/login");
                }
            }
        });
    }
}
