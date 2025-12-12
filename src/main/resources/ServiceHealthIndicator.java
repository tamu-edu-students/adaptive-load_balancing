package com.example.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component("customServiceHealth")
public class ServiceHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        // Simulate a check. In real case, add logic for:
        // queue length, CPU usage, error count etc.
        boolean serviceIsHealthy = true;

        if (serviceIsHealthy) {
            return Health.up().withDetail("status", "Healthy").build();
        } else {
            return Health.down().withDetail("status", "Queue overflow or high error rate").build();
        }
    }
}
