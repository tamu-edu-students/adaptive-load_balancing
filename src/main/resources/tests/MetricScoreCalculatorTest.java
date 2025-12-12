package com.example.metrics;

import com.example.metrics.model.InstanceMetrics;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MetricScoreCalculatorTest {

    private final MetricScoreCalculator calculator = new MetricScoreCalculator();

    @Test
    void testScoreCalculationIdealMetrics() {
        InstanceMetrics metrics = new InstanceMetrics("instance1", 50.0, 0.0, 5, System.currentTimeMillis());
        double score = calculator.calculateScore(metrics);
        assertTrue(score > 90.0);
    }

    @Test
    void testScoreCalculationHighLatency() {
        InstanceMetrics metrics = new InstanceMetrics("instance1", 1500.0, 0.0, 5, System.currentTimeMillis());
        double score = calculator.calculateScore(metrics);
        assertTrue(score < 60.0);
    }

    @Test
    void testScoreCalculationHighErrorRate() {
        InstanceMetrics metrics = new InstanceMetrics("instance1", 100.0, 1.0, 5, System.currentTimeMillis());
        double score = calculator.calculateScore(metrics);
        assertTrue(score < 80.0);
    }

    @Test
    void testScoreCalculationHighQueue() {
        InstanceMetrics metrics = new InstanceMetrics("instance1", 100.0, 0.0, 100, System.currentTimeMillis());
        double score = calculator.calculateScore(metrics);
        assertTrue(score < 80.0);
    }
}
