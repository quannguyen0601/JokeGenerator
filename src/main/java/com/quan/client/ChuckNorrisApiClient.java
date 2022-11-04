package com.quan.client;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import ru.vyarus.dropwizard.guice.module.yaml.bind.Config;

import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Singleton
public class ChuckNorrisApiClient {

    private final Client client;
    private final String requestUri;

    @Inject
    public ChuckNorrisApiClient(@Config("endpoint") String requestUri, @Named("jersey-client") Client client) {
        this.client = client;
        this.requestUri = requestUri;
    }

    public Jokes getJoke(String keyword) {
        WebTarget webTarget = client.target(requestUri).queryParam("query", keyword);
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();

        if (response.getStatus() == 200) {
            return response.readEntity(Jokes.class);
        }

        return null;
    }

    public static class Jokes {
        @JsonProperty("total")
        private int total;

        @JsonProperty("result")
        private List<JokeResult> result = new ArrayList<>();

        public Jokes() {
        }

        public List<JokeResult> getResult() {
            return result;
        }

        public void setResult(List<JokeResult> result) {
            this.result = result;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }
    }

    public static class JokeResult {

        @JsonProperty("created_at")
        @JsonFormat(pattern = "yyyy-mm-dd hh:mm:ss")
        private Date createdAt;
        @JsonProperty("id")
        private String id;
        @JsonProperty("value")
        private String value;
        @JsonProperty("updated_at")
        @JsonFormat(pattern = "yyyy-mm-dd hh:mm:ss")
        private Date updatedAt;

        public JokeResult() {
        }

        public Date getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(Date createdAt) {
            this.createdAt = createdAt;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public Date getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(Date updatedAt) {
            this.updatedAt = updatedAt;
        }
    }
}
