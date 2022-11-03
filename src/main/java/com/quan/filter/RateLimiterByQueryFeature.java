package com.quan.filter;

import com.google.inject.Inject;
import com.quan.service.RateLimiterService;
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
public class RateLimiterByQueryFeature implements DynamicFeature {
    private static final Logger logger = LoggerFactory.getLogger(RateLimiterByQueryFeature.class);

    private final RateLimiterService rateLimiterService;

    @Inject
    public RateLimiterByQueryFeature(RateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }


    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext featureContext) {
        AnnotatedMethod method = new AnnotatedMethod(resourceInfo.getResourceMethod());

        RateLimiterByQueryRequired rateLimit = method.getAnnotation(RateLimiterByQueryRequired.class);

        if(!Objects.isNull(rateLimit)) {

            if(rateLimit.timeLimit() <= 0 || rateLimit.rateLimit() <= 0) {
                throw new IllegalArgumentException("RateLimiterByQueryRequired timeLimit must greater than 0. ");
            }

            if (StringUtils.isBlank(rateLimit.parameter())) {
                throw new IllegalArgumentException("RateLimiterByQueryRequired requires not blank parameter. ");
            }

            featureContext.register(new RateLimiterFilter(rateLimit, resourceInfo, rateLimiterService));
        }

    }
}