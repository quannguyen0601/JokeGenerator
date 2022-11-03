package com.quan.service.Impl;

import com.quan.service.RateLimiterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class SlidingWindowRateLimitServiceImpl implements RateLimiterService {

    private static final Logger logger = LoggerFactory.getLogger(SlidingWindowRateLimitServiceImpl.class);

    public final long NUMBER_OF_TIME_WINDOWS = 10;

    private final Map<String, RequestData> requestTimeMap = new ConcurrentHashMap<>();

    public boolean checkAndAddForExistingKeys(long currentTimeWindow, RequestData requestData) {
        long countInOverallTime = removeOldEntriesForKey(currentTimeWindow, requestData);

        if (countInOverallTime < requestData.getRateLimit()) {
            //Handle new time windows
            Map<Long, AtomicLong> timeWindowVSCountMap = requestData.getTimeWindowVSCountMap();
            long newCount = timeWindowVSCountMap.getOrDefault(currentTimeWindow, new AtomicLong(0)).longValue() + 1;
            timeWindowVSCountMap.put(currentTimeWindow, new AtomicLong(newCount));
            logger.debug("CurrentTimeWindow:" + currentTimeWindow + " Result:true " + " Count:" + countInOverallTime);
            return true;
        }
        logger.debug("CurrentTimeWindow:" + currentTimeWindow + " Result:false " + " Count:" + countInOverallTime);
        return false;
    }

    public long removeOldEntriesForKey(long currentTimeWindow, RequestData requestData) {
        List<Long> oldEntriesToBeDeleted = new ArrayList<>();
        long overallCount = 0L;
        for (Long timeWindow : requestData.getTimeWindowVSCountMap().keySet()) {
            if ((currentTimeWindow - timeWindow) >= requestData.getTimeLimit() / NUMBER_OF_TIME_WINDOWS)
                oldEntriesToBeDeleted.add(timeWindow);
            else
                overallCount += requestData.getTimeWindowVSCountMap().get(timeWindow).longValue();
        }
        oldEntriesToBeDeleted.forEach(requestData.getTimeWindowVSCountMap().keySet()::remove);
        return overallCount;
    }

    @Override
    public void create(String key, int timeLimit, int rateLimit) {
        if (!requestTimeMap.containsKey(key)) {
            long currentTime = Instant.now().getEpochSecond() / NUMBER_OF_TIME_WINDOWS;

            Map<Long, AtomicLong> individualUserHits = new HashMap<>();
            individualUserHits.put(currentTime, new AtomicLong(0L));
            requestTimeMap.put(key, new RequestData(individualUserHits, rateLimit, timeLimit));
            logger.debug("CurrentTimeWindow:" + currentTime + " Result:true " + " Count:0");
        }
    }

    @Override
    public boolean tryAcquire(String key) {
        long currentTime = Instant.now().getEpochSecond() / NUMBER_OF_TIME_WINDOWS;
        //Reference for individual user count data
        RequestData requestData = requestTimeMap.get(key);
        return checkAndAddForExistingKeys(currentTime, requestData);

    }


    static class RequestData {

        private Map<Long, AtomicLong> timeWindowVSCountMap;
        private long rateLimit;
        private long timeLimit;

        public RequestData(Map<Long, AtomicLong> timeWindowVSCountMap, long rateLimit, long timeLimit) {
            this.timeWindowVSCountMap = timeWindowVSCountMap;
            this.rateLimit = rateLimit;
            this.timeLimit = timeLimit;
        }

        public Map<Long, AtomicLong> getTimeWindowVSCountMap() {
            return timeWindowVSCountMap;
        }

        public void setTimeWindowVSCountMap(Map<Long, AtomicLong> timeWindowVSCountMap) {
            this.timeWindowVSCountMap = timeWindowVSCountMap;
        }

        public long getRateLimit() {
            return rateLimit;
        }

        public void setRateLimit(long rateLimit) {
            this.rateLimit = rateLimit;
        }

        public long getTimeLimit() {
            return timeLimit;
        }

        public void setTimeLimit(long timeLimit) {
            this.timeLimit = timeLimit;
        }
    }
}
