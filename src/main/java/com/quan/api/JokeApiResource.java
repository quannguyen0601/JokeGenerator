package com.quan.api;


import com.google.inject.Inject;
import ru.vyarus.dropwizard.guice.module.yaml.bind.Config;

import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;

@Path("/joke")
@Produces("application/json")
public class JokeApiResource {

    public final String serviceName;
    private final Client client;

    @Inject
    public JokeApiResource(@Config("serviceName") String serviceName, @Named("jersey-client") Client client) {
        this.serviceName = serviceName;
        this.client = client;
    }

    @GET
    @Path("/generate")
    public Response ask() {
        return Response.ok(serviceName).build();
    }
}
