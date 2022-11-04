package com.quan.filter;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.quan.service.RateLimitService;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.server.model.AnnotatedMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;
import java.util.Objects;

@Provider
public class RateLimitByQueryFeature implements DynamicFeature {
    private static final Logger logger = LoggerFactory.getLogger(RateLimitByQueryFeature.class);

    private final RateLimitService rateLimitService;

    @Inject
    public RateLimitByQueryFeature(@Named("SlidingWindowRateLimitService") RateLimitService rateLimitService) {
        this.rateLimitService = rateLimitService;
    }

    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext featureContext) {
        AnnotatedMethod method = new AnnotatedMethod(resourceInfo.getResourceMethod());
        RateLimitByQueryRequired rateLimit = method.getAnnotation(RateLimitByQueryRequired.class);

        if (!Objects.isNull(rateLimit)) {

            if (rateLimit.timeLimit() <= 0 || rateLimit.rateLimit() <= 0) {
                throw new IllegalArgumentException("RateLimitByQueryRequired timeLimit must greater than 0. ");
            }

            if (StringUtils.isBlank(rateLimit.parameter())) {
                throw new IllegalArgumentException("RateLimitByQueryRequired requires not blank parameter. ");
            }

            featureContext.register(new RateLimitFilter(rateLimit, resourceInfo, rateLimitService));
        }

    }
}