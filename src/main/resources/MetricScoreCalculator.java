package com.example.metrics;

import org.springframework.stereotype.Component;

@Component
public class MetricScoreCalculator {

    // Tunable weights (mention these in your report)
    private static final double LATENCY_WEIGHT = 0.5;
    private static final double QUEUE_WEIGHT = 0.3;
    private static final double ERROR_WEIGHT = 0.2;

    /**
     * Compute a composite score for a service instance.
     * Lower score = better candidate.
     */
    public double calculateScore(double latencyMs, int queueLength, int errorCount) {

        double normalizedLatency = normalizeLatency(latencyMs);
        double normalizedQueue = normalizeQueue(queueLength);
        double normalizedErrors = normalizeErrors(errorCount);

        return (LATENCY_WEIGHT * normalizedLatency)
                + (QUEUE_WEIGHT * normalizedQueue)
                + (ERROR_WEIGHT * normalizedErrors);
    }

    private double normalizeLatency(double latencyMs) {
        // Prevent unbounded growth
        return Math.min(latencyMs / 1000.0, 1.0);
    }

    private double normalizeQueue(int queueLength) {
        r
