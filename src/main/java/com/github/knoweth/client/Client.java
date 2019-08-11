package com.github.knoweth.client;

import com.github.knoweth.client.services.LoginBody;
import com.github.knoweth.client.services.StorageService;
import com.github.knoweth.client.services.UserService;
import com.github.knoweth.client.views.*;
import org.teavm.flavour.rest.RESTClient;
import org.teavm.flavour.templates.BindTemplate;
import org.teavm.flavour.widgets.ApplicationTemplate;
import org.teavm.flavour.widgets.BackgroundWorker;
import org.teavm.flavour.widgets.RouteBinder;

@BindTemplate("templates/client.html")
public class Client extends ApplicationTemplate implements Routes {
    public static void main(String[] args) {
        UserService r = RESTClient.factory(UserService.class).createResource("");
        BackgroundWorker worker = new BackgroundWorker();
        worker.run(() -> r.login(new LoginBody("bob", "bob")));
        Client client = new Client();
        new RouteBinder()
                .withDefault(Routes.class, Routes::index)
                .add(client)
                .update();

        client.bind("application-content");
    }

    @Override
    public void index() {
        setView(new IndexView());
    }

    @Override
    public void about() {
        setView(new AboutView());
    }

    @Override
    public void documents() {
        setView(new DocumentsView());
    }

    @Override
    public void registration() {
        setView(new RegistrationView());
    }
}

