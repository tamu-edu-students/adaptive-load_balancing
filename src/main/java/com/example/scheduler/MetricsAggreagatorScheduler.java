package com.example.scheduler;

import com.example.metrics.MetricRepository;
import com.example.metrics.model.InstanceMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;

@Component
public class MetricAggregatorScheduler {

    private static final Logger logger = LoggerFactory.getLogger(MetricAggregatorScheduler.class);

    private final MetricRepository metricRepository;

    // Services you're tracking. This can be dynamic via discovery in future
    private static final List<String> SERVICE_IDS = List.of("userservice", "paymentservice", "orderservice");

    // Metrics older than this are considered stale (ms)
    private static final long METRIC_TTL_MS = 30_000;

    public MetricAggregatorScheduler(MetricRepository metricRepository) {
        this.metricRepository = metricRepository;
    }

    @Scheduled(fixedRate = 10_000)
    public void aggregateMetrics() {
        logger.info("Running scheduled metric aggregation");

        for (String serviceId : SERVICE_IDS) {
            Map<String, InstanceMetrics> metricsMap = metricRepository.getAllMetricsForService(serviceId);

            for (Map.Entry<String, InstanceMetrics> entry : metricsMap.entrySet()) {
                String instanceId = entry.getKey();
                InstanceMetrics metrics = entry.getValue();

                if (isStale(metrics)) {
                    logger.warn("Removing stale metrics for {} - {}", serviceId, instanceId);
                    metricRepository.deleteMetrics(serviceId, instanceId);
                    continue;
                }

                InstanceMetrics smoothed = smoothMetrics(metrics);
                metricRepository.saveMetrics(serviceId, smoothed);
            }
        }
    }

    private boolean isStale(InstanceMetrics metrics) {
        long age = Instant.now().toEpochMilli() - metrics.getLastUpdatedEpochMs();
        return age > METRIC_TTL_MS;
    }

    private InstanceMetrics smoothMetrics(InstanceMetrics raw) {
        // Here you can add rolling average logic or EWMA
        // For now, it's a pass-through, but extensible

        return new InstanceMetrics(
                raw.getInstanceId(),
                raw.getAverageLatencyMs(),
                raw.getErrorRate(),
                raw.getQueueLength(),
                Instant.now().toEpochMilli()
        );
    }
}
