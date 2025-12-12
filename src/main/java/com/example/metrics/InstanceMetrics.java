package com.example.metrics.model;

import java.io.Serializable;

public class InstanceMetrics implements Serializable {

    private String instanceId;
    private double averageLatencyMs;
    private double errorRate;
    private int queueLength;
    private long lastUpdatedEpochMs;

    public InstanceMetrics() {}

    public InstanceMetrics(String instanceId,
                           double averageLatencyMs,
                           double errorRate,
                           int queueLength,
                           long lastUpdatedEpochMs) {
        this.instanceId = instanceId;
        this.averageLatencyMs = averageLatencyMs;
        this.errorRate = errorRate;
        this.queueLength = queueLength;
        this.lastUpdatedEpochMs = lastUpdatedEpochMs;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public double getAverageLatencyMs() {
        return averageLatencyMs;
    }

    public double getErrorRate() {
        return errorRate;
    }

    public int getQueueLength() {
        return queueLength;
    }

    public long getLastUpdatedEpochMs() {
        return lastUpdatedEpochMs;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public void setAverageLatencyMs(double averageLatencyMs) {
        this.averageLatencyMs = averageLatencyMs;
    }

    public void setErrorRate(double errorRate) {
        this.errorRate = errorRate;
    }

    public void setQueueLength(int queueLength) {
        this.queueLength = queueLength;
    }

    public void setLastUpdatedEpochMs(long lastUpdatedEpochMs) {
        this.lastUpdatedEpochMs = lastUpdatedEpochMs;
    }
}
