package com.quan.resources;


import com.quan.service.IJokeService;
import com.quan.service.JokeService;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.ws.rs.core.Response;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(DropwizardExtensionsSupport.class)
public class JokeApiResourceTest {
    private static final JokeService JOKE_SERVICE = mock(JokeService.class);

    private static final ResourceExtension EXT = ResourceExtension.builder()
            .addResource(new JokeApiResource(JOKE_SERVICE))
            .build();

    private IJokeService.BaseResponse response;

    @BeforeEach
    void setup() {
        response = new IJokeService.BaseResponse();
        response.setJokes(Arrays.asList("There is in fact an 'I' in Norris, but there is no 'team'. Not even close.",
                "The term \\\"Cleveland Steamer\\\" got its name from Chuck Norris, when he took a dump while visiting the Rock and Roll Hall of fame and buried northern Ohio under a glacier of fecal matter.",
                "Whwn steaming clams, Chuck Norris boils the water by dipping his testicles into the pot. This of course also gives the clams their incredible flavor."));
    }

    @AfterEach
    void tearDown() {
        reset(JOKE_SERVICE);
    }

    @Test
    void getJokeSuccess() {
        when(JOKE_SERVICE.getJokes(any())).thenReturn(response);

        Response response = EXT.target("/joke/generate").queryParam("query","team").request().get(Response.class);

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
    }

    @Test
    void whenQueryIsNull_thenGetFail() {
        when(JOKE_SERVICE.getJokes(any())).thenReturn(response);

        Response response = EXT.target("/joke/generate").queryParam("query","").request().get(Response.class);

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.BAD_REQUEST);
        assertThat(response.readEntity(String.class)).contains("Keyword is blank.");
    }
}
