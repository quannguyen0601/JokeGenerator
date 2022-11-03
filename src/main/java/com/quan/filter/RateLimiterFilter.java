package com.quan.filter;

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

public class RateLimiterFilter implements ContainerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RateLimiterFilter.class);

    private static final int TOO_MANY_REQUESTS = 429;

    private final ResourceInfo resourceInfo;
    private final RateLimiterService rateLimiterService;
    private final RateLimiterByQueryRequired rateLimit;
    public RateLimiterFilter(RateLimiterByQueryRequired rateLimit, ResourceInfo resourceInfo, RateLimiterService rateLimiterService) {
        this.rateLimit = rateLimit;
        this.rateLimiterService = rateLimiterService;
        this.resourceInfo = resourceInfo;
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {
        MultivaluedMap<String, String> queryParameters = containerRequestContext.getUriInfo().getQueryParameters();
        List<String> values = queryParameters.get(rateLimit.parameter());
        if(!values.isEmpty()) {
            //Get value first element in query
            String keyword = values.get(0);
            String rateLimitKey = getRateLimitKey(keyword);
            double requestPerSecond = (double) rateLimit.ratePerMinute() / 60;

            rateLimiterService.setRateLimiter(rateLimitKey, requestPerSecond);

            if(!rateLimiterService.tryAcquire(rateLimitKey)) {
                Exception cause = new IllegalAccessException("Slow down, too many request trying to search same query. " + resourceInfo.getResourceMethod().getName());
                throw new WebApplicationException(cause, Response.status(TOO_MANY_REQUESTS).build());
            }

        }
    }

    private String getRateLimitKey(String searchKeyword) {
        return resourceInfo.getResourceMethod().getName() + "-" + searchKeyword;
    }
}
