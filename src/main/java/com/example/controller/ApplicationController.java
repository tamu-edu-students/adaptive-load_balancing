package com.example.controller;

import com.example.metrics.MetricRepository;
import com.example.metrics.MetricScoreCalculator;
import com.example.metrics.model.InstanceMetrics;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/debug")
public class ApplicationController {

    private final MetricRepository metricRepository;
    private final MetricScoreCalculator scoreCalculator;

    public DebugController(MetricRepository metricRepository,
                           MetricScoreCalculator scoreCalculator) {
        this.metricRepository = metricRepository;
        this.scoreCalculator = scoreCalculator;
    }

    @GetMapping("/metrics/{serviceId}")
    public Map<String, Object> getServiceMetrics(@PathVariable String serviceId) {
        Map<String, InstanceMetrics> rawMetrics = metricRepository.getAllMetricsForService(serviceId);

        Map<String, Object> response = new LinkedHashMap<>();
        for (Map.Entry<String, InstanceMetrics> entry : rawMetrics.entrySet()) {
            InstanceMetrics metrics = entry.getValue();
            double score = scoreCalculator.calculateScore(metrics);

            Map<String, Object> instanceData = new LinkedHashMap<>();
            instanceData.put("latency", metrics.getAverageLatencyMs());
            instanceData.put("errorRate", metrics.getErrorRate());
            instanceData.put("queueLength", metrics.getQueueLength());
            instanceData.put("lastUpdated", metrics.getLastUpdatedEpochMs());
            instanceData.put("calculatedScore", score);

            response.put(entry.getKey(), instanceData);
        }

        return response;
    }
}
