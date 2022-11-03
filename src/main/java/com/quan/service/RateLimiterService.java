package com.quan.service;

public interface RateLimiterService {

    void create(String key, int timeLimit, int rateLimit);

    boolean tryAcquire(String key);
}
