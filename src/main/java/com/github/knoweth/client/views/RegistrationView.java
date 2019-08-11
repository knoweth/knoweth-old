package com.github.knoweth.client.views;

import com.github.knoweth.client.services.LoginBody;
import com.github.knoweth.client.services.UserService;
import org.teavm.flavour.rest.RESTClient;
import org.teavm.flavour.templates.BindTemplate;
import org.teavm.flavour.widgets.BackgroundWorker;

@BindTemplate("templates/registration.html")
public class RegistrationView {
    public void onSubmit() {
        UserService r = RESTClient.factory(UserService.class).createResource("");
        BackgroundWorker worker = new BackgroundWorker();
        // test
        worker.run(() -> r.login(new LoginBody("bob", "bob")));
        throw new IllegalArgumentException("e");
    }
}


