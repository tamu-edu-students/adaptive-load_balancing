package com.example.metrics;

import com.example.metrics.model.InstanceMetrics;

import java.util.Map;
import java.util.Optional;

public interface MetricRepository {

    void saveMetrics(String serviceId, InstanceMetrics metrics);

    Optional<InstanceMetrics> getMetrics(String serviceId, String instanceId);

    Map<String, InstanceMetrics> getAllMetricsForService(String serviceId);

    void deleteMetrics(String serviceId, String instanceId);
}
