package com.github.knoweth.client.services;

import javax.ws.rs.BeanParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Path("")
public interface UserService {
    @Path("login")
    @POST
    void login(LoginBody body);
}
