package com.quan;

import com.quan.configuration.JokeGeneratorConfiguration;
import com.quan.health.PropertiesHealthCheck;
import com.quan.health.ServiceHealthCheck;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import ru.vyarus.dropwizard.guice.GuiceBundle;

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

        // Using DI by using Guice.
        bootstrap.addBundle(GuiceBundle.builder()
                .enableAutoConfig(getClass().getPackage().getName())
                .build());
    }

    @Override
    public void run(final JokeGeneratorConfiguration configuration,
                    final Environment environment) {


        // Register HealthCheck
        ServiceHealthCheck healthCheck = new ServiceHealthCheck();
        PropertiesHealthCheck propertiesHealthCheck = new PropertiesHealthCheck(configuration.getServiceName());

        environment.healthChecks().register("service-health",healthCheck);
        environment.healthChecks().register("properties-health",propertiesHealthCheck);
    }

}
