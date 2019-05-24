package space.potatofrom.amgi.server;

import io.javalin.Javalin;
import io.javalin.staticfiles.Location;

public class Server {
    public static void main(String[] args) {
        Javalin app = Javalin.create();
        app.enableStaticFiles("target/generated/js", Location.EXTERNAL);
        app.enableStaticFiles("src/main/webapp", Location.EXTERNAL);
        app.start(7000);
    }
}
