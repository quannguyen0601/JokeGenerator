package com.quan.health;

import com.codahale.metrics.health.HealthCheck;

public class PropertiesHealthCheck extends HealthCheck {

    private final String serviceName;

    public PropertiesHealthCheck(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    protected Result check() throws Exception {
        if (serviceName.isEmpty()) {
            return Result.unhealthy("Not Load Properties.");
        }
        return Result.healthy(serviceName + " Stay Strong!");
    }
}
