package com.github.knoweth.client;

import com.github.knoweth.client.cache.DataCache;
import com.github.knoweth.client.views.*;
import com.github.knoweth.server.auth.User;
import org.teavm.flavour.templates.BindTemplate;
import org.teavm.flavour.widgets.ApplicationTemplate;
import org.teavm.flavour.widgets.BackgroundWorker;
import org.teavm.flavour.widgets.RouteBinder;
import org.teavm.jso.browser.Window;

@BindTemplate("templates/client.html")
public class Client extends ApplicationTemplate implements Routes {
    private final DataCache dataCache = new DataCache();

    public static void main(String[] args) {
        Client client = new Client();
        new RouteBinder()
                .withDefault(Routes.class, Routes::index)
                .add(client)
                .update();

        client.bind("application-content");
    }

    public String getUsername() {
        if (!dataCache.isUserLoaded()) {
            new BackgroundWorker().run(dataCache::loadCurrentUser);
            return null;
        } else {
            User user = dataCache.getCurrentUser();
            if (user == null) {
                return null;
            } else {
                return user.getUsername();
            }
        }
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
        setView(new DocumentsView(dataCache));
    }

    @Override
    public void registration() {
        setView(new RegistrationView());
    }

    @Override
    public void login() {
        setView(new LoginView());
    }
}

