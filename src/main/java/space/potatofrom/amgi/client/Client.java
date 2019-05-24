package space.potatofrom.amgi.client;

import org.teavm.flavour.templates.BindTemplate;
import org.teavm.flavour.widgets.ApplicationTemplate;
import org.teavm.flavour.widgets.RouteBinder;

@BindTemplate("templates/client.html")
public class Client extends ApplicationTemplate implements Routes {
    public static void main(String[] args) {
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
    public void hello(String name) {
//        setView(new HelloView(name));
    }

    @Override
    public void goodbye() {
//        setView(new GoodbyeView());
    }


}

