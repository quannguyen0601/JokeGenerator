package com.quan.api;


import com.google.inject.Inject;
import ru.vyarus.dropwizard.guice.module.yaml.bind.Config;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/joke")
@Produces("application/json")
public class JokerResource {

    public final String serviceName;

    @Inject
    public JokerResource(@Config("serviceName") String serviceName) {
        this.serviceName = serviceName;
    }

    @GET
    @Path("/generate")
    public Response ask() {
        return Response.ok(serviceName).build();
    }
}
