package com.github.knoweth.client.services;

import com.github.knoweth.common.Response;
import com.github.knoweth.server.auth.User;
import org.teavm.flavour.json.JsonPersistable;
import org.teavm.flavour.rest.Resource;

import javax.ws.rs.*;

@Path("")
@Resource
public interface UserService {

    @JsonPersistable
    class LoginBody {
        public LoginBody(String username, String password) {
            this.username = username;
            this.password = password;
        }

        String username;
        String password;

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }


    @Path("login")
    @POST
    void login(LoginBody body);

    @Path("api/users/register")
    @POST
    Response register(User body);

    @Path("api/users/status")
    @GET
    User status();
}
