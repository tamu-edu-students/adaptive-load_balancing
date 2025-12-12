package com.example.loadbalancer;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RoundRobinLoadBalancer {

    private final ConcurrentHashMap<String, AtomicInteger> counters = new ConcurrentHashMap<>();

    public String selectInstance(String serviceId, List<String> instanceIds) {
        if (instanceIds.isEmpty()) {
            return null;
        }

        AtomicInteger counter = counters.computeIfAbsent(serviceId, k -> new AtomicInteger(0));
        int index = Math.abs(counter.getAndIncrement()) % instanceIds.size();
        return instanceIds.get(index);
    }
}
