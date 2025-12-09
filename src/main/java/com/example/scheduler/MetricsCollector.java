package com.example.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class MetricsCollector {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private final RestTemplate restTemplate = new RestTemplate();

    @Scheduled(fixedRate = 5000)
    public void updateMetrics() {
        Map<String, String> serviceUrls = Map.of(
            "service-a", "http://localhost:8080/actuator/prometheus",
            "service-b", "http://localhost:8080/actuator/prometheus"
        );

        serviceUrls.forEach((name, url) -> {
            redisTemplate.opsForValue().set(name + ":latency", String.valueOf(Math.random() * 100));
        });
    }
}
