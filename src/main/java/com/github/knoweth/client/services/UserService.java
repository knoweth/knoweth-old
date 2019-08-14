package com.github.knoweth.client.services;

import com.github.knoweth.common.Response;
import org.teavm.flavour.json.JsonPersistable;
import org.teavm.flavour.rest.Resource;

import javax.ws.rs.BeanParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Path("")
@Resource
public interface UserService {
    @JsonPersistable
    class RegistrationBody {
        public RegistrationBody(String username, String password) {
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
    Response register(RegistrationBody body);
}
