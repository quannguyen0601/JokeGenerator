package com.quan.configuration;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.quan.service.Impl.JokeServiceImpl;
import com.quan.service.Impl.SlidingWindowRateLimitServiceImpl;
import com.quan.service.JokeService;
import com.quan.service.RateLimitService;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.setup.Environment;

import javax.inject.Named;
import javax.ws.rs.client.Client;

public class ServiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(JokeService.class).to(JokeServiceImpl.class);
//        bind(RateLimiterService.class).to(TokenBucketRateLimitServiceImpl.class);
        bind(RateLimitService.class).to(SlidingWindowRateLimitServiceImpl.class);
    }

    @Provides
    @Named("jersey-client")
    Client getClient(Environment environment, JokeGeneratorConfiguration configuration) {
        return new JerseyClientBuilder(environment).using(configuration.getJerseyClientConfiguration())
                .build("Jersey-Client");
    }
}
