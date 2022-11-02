package com.quan.configuration;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.setup.Environment;

import javax.inject.Named;
import javax.ws.rs.client.Client;

public class ServiceModule  extends AbstractModule {


    @Override
    protected void configure() {
    }

    @Provides
    @Named("jersey-client")
    Client getClient(Environment environment, JokeGeneratorConfiguration configuration) {
        return new JerseyClientBuilder(environment).using(configuration.getJerseyClientConfiguration())
                .build("Jersey-Client");
    }
}
