package com.quan.filter;

import com.google.inject.name.Named;
import com.quan.api.BaseResponse;
import com.quan.service.RateLimitService;
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

public class RateLimitFilter implements ContainerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RateLimitFilter.class);

    private static final int TOO_MANY_REQUESTS = 429;

    private final ResourceInfo resourceInfo;
    private final RateLimitService rateLimitService;
    private final RateLimitByQueryRequired rateLimitByQuery;

    public RateLimitFilter(RateLimitByQueryRequired rateLimitByQuery, ResourceInfo resourceInfo,
                           RateLimitService rateLimitService) {
        this.rateLimitByQuery = rateLimitByQuery;
        this.rateLimitService = rateLimitService;
        this.resourceInfo = resourceInfo;
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {
        MultivaluedMap<String, String> queryParameters = containerRequestContext.getUriInfo().getQueryParameters();
        List<String> values = queryParameters.get(rateLimitByQuery.parameter());
        if (!Objects.isNull(values) && !values.isEmpty()) {
            //Get value first element in query
            String keyword = values.get(0);
            String rateLimitKey = getRateLimitKey(keyword);

            rateLimitService.create(rateLimitKey, rateLimitByQuery.timeLimit(), rateLimitByQuery.rateLimit());

            if (!rateLimitService.tryAcquire(rateLimitKey)) {
                logger.debug("RateLimit hit. for method(): {} query: {}", resourceInfo.getResourceMethod().getName(), keyword);
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
