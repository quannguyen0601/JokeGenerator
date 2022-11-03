package com.quan.service.Impl;

import com.google.common.util.concurrent.RateLimiter;
import com.quan.service.RateLimiterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryRateLimitServiceImpl implements RateLimiterService {

    private static final Logger logger = LoggerFactory.getLogger(InMemoryRateLimitServiceImpl.class);

    private Map<String, RateLimiter> rateLimiterMap = new ConcurrentHashMap<>();

    @Override
    public void setRateLimiter(String key, double ratePerSecond) {
        RateLimiter rateLimiter =  RateLimiter.create(ratePerSecond);
        rateLimiterMap.putIfAbsent(key, rateLimiter);
    }

    @Override
    public boolean tryAcquire(String key) {
       RateLimiter rateLimiter = rateLimiterMap.get(key);
       return rateLimiter.tryAcquire();
    }
}
