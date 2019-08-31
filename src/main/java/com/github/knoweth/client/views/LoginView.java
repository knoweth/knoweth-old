package com.github.knoweth.client.views;

import org.teavm.flavour.templates.BindTemplate;
import org.teavm.jso.browser.Location;

/**
 * State for the login view.
 */
@BindTemplate("templates/login.html")
public class LoginView {
    /**
     * @return whether the last login failed (detectable by the "?error" query param)
     */
    public boolean hasErrored() {
        return Location.current().getSearch().contains("error");
    }
}


