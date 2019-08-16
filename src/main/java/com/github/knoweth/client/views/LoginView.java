package com.github.knoweth.client.views;

import org.teavm.flavour.templates.BindTemplate;
import org.teavm.jso.browser.Location;

@BindTemplate("templates/login.html")
public class LoginView {
    public boolean hasErrored() {
        return Location.current().getSearch().contains("error");
    }
}


