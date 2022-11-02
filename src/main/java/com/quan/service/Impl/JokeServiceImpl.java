package com.quan.service.Impl;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.inject.Inject;
import com.quan.service.JokeService;
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
import java.util.Objects;
import java.util.stream.Collectors;

public class JokeServiceImpl extends JokeService {

    private final Client client;
    private final String requestUri;

    @Inject
    public JokeServiceImpl(@Config("endpoint") String requestUri, @Named("jersey-client") Client client) {
        this.requestUri = requestUri;
        this.client = client;
    }

    @Override
    protected BaseResponse doGetJokes(BaseSearch input) {
        Jokes jokes = getJokeFromApi(input);

        if(Objects.isNull(jokes)) {
            return new BaseResponse();
        }

        List<String> jokesValue = jokes.getResult().stream().map(JokeResult::getValue).collect(Collectors.toList());
        return new BaseResponse(jokesValue);
    }

    private Jokes getJokeFromApi(BaseSearch input) {
        WebTarget webTarget = client.target(requestUri).queryParam("query",input.getKeyword());
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();

        if (response.getStatus() == 200) {
            return response.readEntity(Jokes.class);
        }

        return null;
    }

    static class Jokes {
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

    static class JokeResult {

        public JokeResult() {
        }
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
