package com.quan.service;

import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class JokeService implements IJokeService {
    @Override
    public BaseResponse getJokes(BaseSearch query) {
        BaseResponse result = doGetJokes(query);
        List<String> jokes = result.getJokes().stream().filter(jokeFilter(query)).collect(Collectors.toList());
        return new BaseResponse(jokes);
    }

    protected abstract BaseResponse doGetJokes(BaseSearch input);

    protected Predicate<String> jokeFilter(BaseSearch input) {
        return Pattern
                .compile("\\b"+input.getKeyword()+"\\b")
                .asPredicate();
    }
}
