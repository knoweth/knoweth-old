package com.github.knoweth.client.views;

import org.teavm.flavour.routing.Path;
import org.teavm.flavour.routing.PathParameter;
import org.teavm.flavour.routing.PathSet;
import org.teavm.flavour.routing.Route;

@PathSet
public interface Routes extends Route {
    @Path("/")
    void index();

    @Path("/about")
    void about();

    @Path("/documents/{id}")
    void document(@PathParameter("id") int id);

    @Path("/documents")
    void documents();

    @Path("/registration")
    void registration();

    @Path("/review/{id}")
    void review(@PathParameter("id") int id);

    @Path("/login")
    void login();
}

