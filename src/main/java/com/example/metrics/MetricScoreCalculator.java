package com.example.metrics;

import com.example.metrics.model.InstanceMetrics;
import org.springframework.stereotype.Component;

@Component
public class MetricScoreCalculator {

    private static final double LATENCY_WEIGHT = 0.5;
    private static final double ERROR_RATE_WEIGHT = 0.3;
    private static final double QUEUE_LENGTH_WEIGHT = 0.2;

    private static final double MAX_LATENCY_MS = 1000.0;
    private static final double MAX_ERROR_RATE = 1.0;    // 1.0 = 100%
    private static final int MAX_QUEUE_LENGTH = 100;

    /**
     * Calculates a score between 0.0 and 100.0.
     * Higher is better.
     */
    public double calculateScore(InstanceMetrics metrics) {
        double latencyScore = 1.0 - normalize(metrics.getAverageLatencyMs(), MAX_LATENCY_MS);
        double errorScore = 1.0 - normalize(metrics.getErrorRate(), MAX_ERROR_RATE);
        double queueScore = 1.0 - normalize(metrics.getQueueLength(), MAX_QUEUE_LENGTH);

        double score = (
                (latencyScore * LATENCY_WEIGHT) +
                        (errorScore * ERROR_RATE_WEIGHT) +
                        (queueScore * QUEUE_LENGTH_WEIGHT)
        ) * 100.0;

        return Math.max(score, 0.0);
    }

    private double normalize(double value, double max) {
        if (value <= 0) return 0.0;
        if (value >= max) return 1.0;
        return value / max;
    }
}
