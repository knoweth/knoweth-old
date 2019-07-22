package com.github.knoweth.client;

import org.teavm.flavour.routing.Path;
import org.teavm.flavour.routing.PathSet;
import org.teavm.flavour.routing.Route;

@PathSet
public interface Routes extends Route {
    @Path("/")
    void index();

    @Path("/about")
    void about();
}

