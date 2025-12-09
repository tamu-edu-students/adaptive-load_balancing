package com.example.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class MetricsService {
    private final MeterRegistry registry;
    private final ConcurrentHashMap<String, Timer> timers = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, AtomicInteger> queueLengths = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, AtomicInteger> errorRates = new ConcurrentHashMap<>();

    public MetricsService(MeterRegistry registry) {
        this.registry = registry;
    }

    public void recordLatency(String service, double duration) {
        timers.computeIfAbsent(service, s -> Timer.builder(service + ".latency").register(registry))
              .record((long) duration, java.util.concurrent.TimeUnit.MILLISECONDS);
    }

    public void setQueueLength(String service, int length) {
        queueLengths.computeIfAbsent(service, s ->
                registry.gauge(service + ".queue.length", new AtomicInteger(length)))
                    .set(length);
    }

    public void recordError(String service) {
        errorRates.computeIfAbsent(service, s ->
                registry.gauge(service + ".error.count", new AtomicInteger(0)))
                    .incrementAndGet();
    }
}
