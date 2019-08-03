package com.github.knoweth.client;

import com.github.knoweth.client.services.StorageService;
import com.github.knoweth.client.views.AboutView;
import com.github.knoweth.client.views.DocumentsView;
import com.github.knoweth.client.views.IndexView;
import com.github.knoweth.client.views.Routes;
import org.teavm.flavour.rest.RESTClient;
import org.teavm.flavour.templates.BindTemplate;
import org.teavm.flavour.widgets.ApplicationTemplate;
import org.teavm.flavour.widgets.BackgroundWorker;
import org.teavm.flavour.widgets.RouteBinder;

@BindTemplate("templates/client.html")
public class Client extends ApplicationTemplate implements Routes {
    public static void main(String[] args) {
        // pog
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
}

