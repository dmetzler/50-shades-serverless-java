package org.dmetzler.serverless.jaxrs;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("ping")
public class Ping {
    @GET
    public String get() {
        return "pong";
    }
}
