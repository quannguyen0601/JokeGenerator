package com.quan.api;


import com.google.inject.Inject;
import com.quan.model.BaseResponse;
import com.quan.service.IJokeService;
import com.quan.service.JokeService;
import org.apache.commons.lang3.StringUtils;
import ru.vyarus.dropwizard.guice.module.yaml.bind.Config;

import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;
import java.util.Objects;

@Path("/joke")
@Produces("application/json")
public class JokeApiResource {

    private final JokeService jokeService;

    @Inject
    public JokeApiResource(JokeService jokeService) {
        this.jokeService = jokeService;
    }

    @GET
    @Path("/generate")
    public Response generate(@QueryParam("query") String keyword) {
        if(StringUtils.isBlank(keyword)) {
            return Response.status(400).entity(new BaseResponse<>("Keyword is blank.")).build();
        }
        IJokeService.BaseSearch searchQuery = new IJokeService.BaseSearch(keyword);

        return Response.ok(jokeService.getJokes(searchQuery)).build();
    }


}
