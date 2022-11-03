package com.quan.service.Impl;

import com.google.common.util.concurrent.RateLimiter;
import com.quan.service.RateLimitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TokenBucketRateLimitServiceImpl implements RateLimitService {

    private static final Logger logger = LoggerFactory.getLogger(TokenBucketRateLimitServiceImpl.class);

    private final Map<String, RateLimiter> rateLimiterMap = new ConcurrentHashMap<>();

    @Override
    public void create(String key, int timeLimit, int rateLimit) {
        RateLimiter rateLimiter = RateLimiter.create((double) rateLimit / timeLimit);
        rateLimiterMap.putIfAbsent(key, rateLimiter);
    }

    @Override
    public boolean tryAcquire(String key) {
        RateLimiter rateLimiter = rateLimiterMap.get(key);
        return rateLimiter.tryAcquire();
    }
}
