package com.github.knoweth.client.views;

import com.github.knoweth.client.services.Services;
import com.github.knoweth.client.services.UserService;
import com.github.knoweth.common.Response;
import com.github.knoweth.server.auth.User;
import org.teavm.flavour.templates.BindTemplate;
import org.teavm.flavour.templates.Templates;
import org.teavm.flavour.widgets.BackgroundWorker;
import org.teavm.jso.browser.Location;

import java.util.Timer;
import java.util.TimerTask;

@BindTemplate("templates/registration.html")
public class RegistrationView {
    public boolean isEnabled = true;
    public String message;
    public String error;
    public String username;
    public String email;
    public String password;

    public void onSubmit() {
        isEnabled = false;
        Templates.update();
        BackgroundWorker worker = new BackgroundWorker();
        worker.run(() -> {
            try {
                Response res = Services.USER.register(new User(username, email, password));
                System.out.println(res);
                if (res.getOk()) {
                    message = "Registration complete. Redirecting to login page...";

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Location.current().setHash("/login");
                        }
                    }, 2500);
                } else {
                    error();
                }
            } catch (Exception e) {
                e.printStackTrace();
                error();
            } finally {
                isEnabled = true;
            }
        });
    }

    private void error() {
        error = "Sorry, we were unable to process your registration at this time. Please try again later.";
    }
}


