package com.quan.service;

public interface RateLimiterService {

    void setRateLimiter(String key, double ratePerSecond);

    boolean tryAcquire(String key);
}
