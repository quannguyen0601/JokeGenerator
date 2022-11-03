package com.quan.service.Impl;

import com.google.inject.Inject;
import com.quan.client.ChuckNorrisApiClient;
import com.quan.service.JokeService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class JokeServiceImpl extends JokeService {

    private final ChuckNorrisApiClient client;

    @Inject
    public JokeServiceImpl(ChuckNorrisApiClient client) {
        this.client = client;
    }

    @Override
    protected BaseResponse doGetJokes(BaseSearch input) {
        ChuckNorrisApiClient.Jokes jokes = client.getJoke(input.getKeyword());

        if (Objects.isNull(jokes)) {
            return new BaseResponse();
        }

        List<String> jokesValue = jokes.getResult().stream().map(i -> i.getValue()).collect(Collectors.toList());
        return new BaseResponse(jokesValue);
    }
}
