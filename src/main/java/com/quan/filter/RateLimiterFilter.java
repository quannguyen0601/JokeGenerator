package com.quan.filter;

import com.quan.api.BaseResponse;
import com.quan.service.RateLimiterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Objects;

public class RateLimiterFilter implements ContainerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RateLimiterFilter.class);

    private static final int TOO_MANY_REQUESTS = 429;

    private final ResourceInfo resourceInfo;
    private final RateLimiterService rateLimiterService;
    private final RateLimiterByQueryRequired rateLimitByQuery;
    public RateLimiterFilter(RateLimiterByQueryRequired rateLimitByQuery, ResourceInfo resourceInfo, RateLimiterService rateLimiterService) {
        this.rateLimitByQuery = rateLimitByQuery;
        this.rateLimiterService = rateLimiterService;
        this.resourceInfo = resourceInfo;
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {
        MultivaluedMap<String, String> queryParameters = containerRequestContext.getUriInfo().getQueryParameters();
        List<String> values = queryParameters.get(rateLimitByQuery.parameter());
        if(!Objects.isNull(values) && !values.isEmpty()) {
            //Get value first element in query
            String keyword = values.get(0);
            String rateLimitKey = getRateLimitKey(keyword);

            rateLimiterService.create(rateLimitKey, rateLimitByQuery.timeLimit(), rateLimitByQuery.rateLimit());

            if(!rateLimiterService.tryAcquire(rateLimitKey)) {
                logger.debug("RateLimit hit. for method {} query {}", resourceInfo.getResourceMethod().getName(), keyword);
                Exception cause = new IllegalAccessException("Slow down, too many request trying to search same query. " + resourceInfo.getResourceMethod().getName());
                throw new WebApplicationException(cause,
                        Response.status(TOO_MANY_REQUESTS).entity(new BaseResponse<>("Slow down, too many request trying to search same query."))
                                .build());
            }

        }
    }

    private String getRateLimitKey(String searchKeyword) {
        return resourceInfo.getResourceMethod().getName() + "-" + searchKeyword;
    }
}
