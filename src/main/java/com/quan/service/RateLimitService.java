package com.quan.service;

public interface RateLimitService {

    void create(String key, int timeLimit, int rateLimit);

    boolean tryAcquire(String key);
}
