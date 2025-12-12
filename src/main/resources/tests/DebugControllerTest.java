package com.example.controller;

import com.example.metrics.MetricRepository;
import com.example.metrics.MetricScoreCalculator;
import com.example.metrics.model.InstanceMetrics;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DebugController.class)
class DebugControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MetricRepository metricRepository;

    @MockBean
    private MetricScoreCalculator scoreCalculator;

    @Test
    void shouldReturnMetricsWithScore() throws Exception {
        InstanceMetrics metrics = new InstanceMetrics("localhost:8081", 100.0, 0.05, 10, System.currentTimeMillis());

        when(metricRepository.getAllMetricsForService("userservice"))
                .thenReturn(Map.of("localhost:8081", metrics));

        when(scoreCalculator.calculateScore(metrics)).thenReturn(85.5);

        mockMvc.perform(get("/debug/metrics/userservice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['localhost:8081'].latency").value(100.0))
                .andExpect(jsonPath("$.['localhost:8081'].calculatedScore").value(85.5));
    }
}
