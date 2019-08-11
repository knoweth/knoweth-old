package com.github.knoweth.client.services;

import org.teavm.flavour.rest.Resource;

import javax.ws.rs.BeanParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Path("")
@Resource
public interface UserService {
    @Path("login")
    @POST
    void login(LoginBody body);
}
