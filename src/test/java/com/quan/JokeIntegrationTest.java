package com.quan;

import com.quan.configuration.JokeGeneratorConfiguration;
import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.ws.rs.core.Response;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(DropwizardExtensionsSupport.class)
public class JokeIntegrationTest {
    private static final String CONFIG = "test-config.yml";

    static final DropwizardAppExtension<JokeGeneratorConfiguration> APP = new DropwizardAppExtension<>(
            JokeGeneratorApplication.class, CONFIG,
            new ResourceConfigurationSourceProvider()
    );

    @Test
    void whenRateLimitExceeded_thenGetFail() {

        Response response1 = APP.client().target("http://localhost:" + APP.getLocalPort() + "/api/joke/generate")
                .queryParam("query","team").request().get(Response.class);
        Response response2 = APP.client().target("http://localhost:" + APP.getLocalPort() + "/api/joke/generate")
                .queryParam("query","team").request().get(Response.class);
        Response response3 = APP.client().target("http://localhost:" + APP.getLocalPort() + "/api/joke/generate")
                .queryParam("query","team").request().get(Response.class);
        Response response4 = APP.client().target("http://localhost:" + APP.getLocalPort() + "/api/joke/generate")
                .queryParam("query","team").request().get(Response.class);
        Response response5 = APP.client().target("http://localhost:" + APP.getLocalPort() + "/api/joke/generate")
                .queryParam("query","team").request().get(Response.class);
        Response response6 = APP.client().target("http://localhost:" + APP.getLocalPort() + "/api/joke/generate")
                .queryParam("query","team").request().get(Response.class);

        assertThat(Arrays.asList(response1,response2,response3,response4,response5))
                .filteredOn(response -> response.getStatusInfo().equals(Response.Status.OK));
        assertThat(response6.getStatus()).isEqualTo(429);
    }
}
