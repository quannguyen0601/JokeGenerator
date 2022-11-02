package com.quan;

import com.quan.configuration.JokeGeneratorConfiguration;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class JokeGeneratorApplication extends Application<JokeGeneratorConfiguration> {

    public static void main(final String[] args) throws Exception {
        new JokeGeneratorApplication().run(args);
    }

    @Override
    public String getName() {
        return "JokeGenerator";
    }

    @Override
    public void initialize(final Bootstrap<JokeGeneratorConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final JokeGeneratorConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
    }

}
