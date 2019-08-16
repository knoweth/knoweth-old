package com.github.knoweth.client.services;

import org.teavm.flavour.rest.RESTClient;

public class Services {
    public static final UserService USER = RESTClient.factory(UserService.class).createResource("");
}
