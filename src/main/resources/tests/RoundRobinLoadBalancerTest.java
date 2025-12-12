package com.example.loadbalancer;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoundRobinLoadBalancerTest {

    private final RoundRobinLoadBalancer loadBalancer = new RoundRobinLoadBalancer();

    @Test
    void testRoundRobinSelectionCyclesThroughInstances() {
        List<String> instances = List.of("a", "b", "c");

        String first = loadBalancer.selectInstance("service1", instances);
        String second = loadBalancer.selectInstance("service1", instances);
        String third = loadBalancer.selectInstance("service1", instances);
        String fourth = loadBalancer.selectInstance("service1", instances);

        assertEquals("a", first);
        assertEquals("b", second);
        assertEquals("c", third);
        assertEquals("a", fourth); // should wrap around
    }
}
