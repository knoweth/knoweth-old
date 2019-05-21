package space.potatofrom.amgi;

import io.javalin.Javalin;
import io.javalin.staticfiles.Location;

public class Server {
    public static void main(String[] args) {
        Javalin app = Javalin.create();
        app.enableStaticFiles("target/amgi-1.0-SNAPSHOT", Location.EXTERNAL);
        app.start(7000);
        app.get("/", ctx -> ctx.result("Hello World"));
    }
}
