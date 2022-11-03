package com.quan.resources;


import com.google.inject.Inject;
import com.quan.api.BaseResponse;
import com.quan.filter.RateLimiterByQueryRequired;
import com.quan.service.IJokeService;
import com.quan.service.JokeService;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path("/joke")
@Produces("application/json")
public class JokeApiResource {

    private final JokeService jokeService;

    @Inject
    public JokeApiResource(JokeService jokeService) {
        this.jokeService = jokeService;
    }

    @GET
    @RateLimiterByQueryRequired(parameter = "query", rateLimit = 5, timeLimit = 60)
    @Path("/generate")
    public Response generate(@QueryParam("query") Optional<String> keyword) {
        if (!keyword.isPresent() || StringUtils.isBlank(keyword.get())) {
            return Response.status(400).entity(new BaseResponse<>("Keyword is blank.")).build();
        }
        IJokeService.BaseSearch searchQuery = new IJokeService.BaseSearch(keyword.get());

        return Response.ok(new BaseResponse<>(jokeService.getJokes(searchQuery))).build();
    }
}
