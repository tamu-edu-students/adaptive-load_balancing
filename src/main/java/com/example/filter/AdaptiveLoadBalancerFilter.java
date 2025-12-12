package com.example.filter;

import com.example.metrics.MetricRepository;
import com.example.metrics.MetricScoreCalculator;
import com.example.metrics.model.InstanceMetrics;
import com.example.loadbalancer.RoundRobinLoadBalancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.*;

@Component
public class AdaptiveLoadBalancingFilter extends AbstractGatewayFilterFactory<AdaptiveLoadBalancingFilter.Config> {

    @Autowired
    private MetricRepository metricRepository;

    @Autowired
    private MetricScoreCalculator scoreCalculator;

    @Autowired
    private RoundRobinLoadBalancer roundRobinLoadBalancer;

    public AdaptiveLoadBalancingFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String serviceId = extractServiceId(exchange);
            if (serviceId == null) {
                return chain.filter(exchange);
            }

            List<String> instanceIds = getInstanceIdsForService(serviceId);
            if (instanceIds.isEmpty()) {
                return chain.filter(exchange);
            }

            Map<String, InstanceMetrics> metricsMap = metricRepository.getAllMetricsForService(serviceId);
            Optional<String> bestInstanceId = selectBestInstance(metricsMap);

            String targetInstanceId = bestInstanceId.orElseGet(() ->
                    roundRobinLoadBalancer.selectInstance(serviceId, instanceIds)
            );

            URI originalUri = exchange.getRequest().getURI();
            URI newUri = replaceHost(originalUri, targetInstanceId);

            ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                    .uri(newUri)
                    .build();

            return chain.filter(exchange.mutate().request(mutatedRequest).build());
        };
    }

    private String extractServiceId(ServerWebExchange exchange) {
        // You can inject service ID through route configuration or path prefix
        String host = exchange.getRequest().getHeaders().getFirst("Host");
        if (host != null && host.contains(".")) {
            return host.split("\\.")[0];
        }
        return null;
    }

    private List<String> getInstanceIdsForService(String serviceId) {
        // Replace with discovery or static config as needed
        return List.of("localhost:8081", "localhost:8082", "localhost:8083");
    }

    private Optional<String> selectBestInstance(Map<String, InstanceMetrics> metricsMap) {
        return metricsMap.entrySet().stream()
                .max(Comparator.comparingDouble(e -> scoreCalculator.calculateScore(e.getValue())))
                .map(Map.Entry::getKey);
    }

    private URI replaceHost(URI original, String instanceHost) {
        return URI.create(original.getScheme() + "://" + instanceHost + original.getPath());
    }

    public static class Config {
    }
}
