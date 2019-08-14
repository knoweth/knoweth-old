package com.github.knoweth.common;

import org.teavm.flavour.json.JsonPersistable;

/**
 * Represents a JSON response - either successful or error.
 */
@JsonPersistable
public class Response {
    private boolean ok;
    private String message;

    private Response() {}

    public static Response err(String message) {
        Response r = new Response();
        r.ok = false;
        r.message = message;
        return r;
    }

    public static Response success(String message) {
        Response r = new Response();
        r.ok = true;
        r.message = message;
        return r;
    }

    public boolean getOk() {
        return ok;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Response{" +
                "ok=" + ok +
                ", message='" + message + '\'' +
                '}';
    }
}
