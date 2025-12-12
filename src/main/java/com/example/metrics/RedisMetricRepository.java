package com.example.metrics;

import com.example.metrics.model.InstanceMetrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class RedisMetricRepository implements MetricRepository {

    private static final String KEY_PREFIX = "metrics:";
    private static final Duration METRIC_TTL = Duration.ofSeconds(30);

    @Autowired
    private RedisTemplate<String, InstanceMetrics> redisTemplate;

    @Override
    public void saveMetrics(String serviceId, InstanceMetrics metrics) {
        String key = buildKey(serviceId, metrics.getInstanceId());
        redisTemplate.opsForValue().set(key, metrics, METRIC_TTL);
    }

    @Override
    public Optional<InstanceMetrics> getMetrics(String serviceId, String instanceId) {
        String key = buildKey(serviceId, instanceId);
        return Optional.ofNullable(redisTemplate.opsForValue().get(key));
    }

    @Override
    public Map<String, InstanceMetrics> getAllMetricsForService(String serviceId) {
        Map<String, InstanceMetrics> result = new HashMap<>();

        String pattern = KEY_PREFIX + serviceId + ":*";
        for (String key : redisTemplate.keys(pattern)) {
            InstanceMetrics metrics = redisTemplate.opsForValue().get(key);
            if (metrics != null) {
                result.put(metrics.getInstanceId(), metrics);
            }
        }
        return result;
    }

    @Override
    public void deleteMetrics(String serviceId, String instanceId) {
        redisTemplate.delete(buildKey(serviceId, instanceId));
    }

    private String buildKey(String serviceId, String instanceId) {
        return KEY_PREFIX + serviceId + ":" + instanceId;
    }
}
