package com.example.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@Component
public class AdaptiveLoadBalancerFilter implements GlobalFilter, Ordered {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {
        List<String> services = Arrays.asList("localhost:8080/service-a", "localhost:8080/service-b");
        String bestService = selectServiceBasedOnLatency(services);
        URI newUri = URI.create("http://" + bestService);
        ServerHttpRequest newRequest = exchange.getRequest().mutate().uri(newUri).build();
        return chain.filter(exchange.mutate().request(newRequest).build());
    }

    private String selectServiceBasedOnLatency(List<String> services) {
        return services.get((int) (Math.random() * services.size()));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
