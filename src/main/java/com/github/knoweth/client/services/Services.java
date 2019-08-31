package com.github.knoweth.client.services;

import org.teavm.flavour.rest.RESTClient;

/**
 * A Giant Global Object that enables simple access to backend REST services
 * for the client.
 *
 * Use the static properties to access various REST methods.
 */
public class Services {
    public static final UserService USER = RESTClient.factory(UserService.class).createResource("");
    public static final StorageService STORAGE = RESTClient.factory(StorageService.class).createResource("api");
}
