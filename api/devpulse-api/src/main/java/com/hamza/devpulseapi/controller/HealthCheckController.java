package com.hamza.devpulseapi.controller;

import com.hamza.devpulseapi.model.HealthCheck;
import com.hamza.devpulseapi.service.EndpointService;
import com.hamza.devpulseapi.service.HealthCheckService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class HealthCheckController {

    private final HealthCheckService healthCheckService;
    private final EndpointService endpointService;

    public HealthCheckController(HealthCheckService healthCheckService,
                                 EndpointService endpointService) {
        this.healthCheckService = healthCheckService;
        this.endpointService = endpointService;
    }

    // Internal endpoint — called by Go monitor, not by users
    @PostMapping("/internal/health-checks")
    public ResponseEntity<HealthCheck> record(@RequestBody RecordRequest request) {
        HealthCheck result = healthCheckService.recordResult(
                request.endpointId(),
                request.status(),
                request.statusCode(),
                request.responseMs()
        );
        return ResponseEntity.status(201).body(result);
    }

    // User-facing — get last 20 checks for one of their endpoints
    @GetMapping("/api/endpoints/{id}/history")
    public List<HealthCheck> getHistory(@PathVariable Long id) {
        endpointService.getById(id); // ownership check
        return healthCheckService.getHistory(id);
    }

    // User-facing — get average response time
    @GetMapping("/api/endpoints/{id}/stats")
    public ResponseEntity<Map<String, Object>> getStats(@PathVariable Long id) {
        endpointService.getById(id); // ownership check
        Double avg = healthCheckService.getAvgResponseMs(id);
        return ResponseEntity.ok(Map.of(
                "endpointId", id,
                "avgResponseMs", avg != null ? Math.round(avg) : 0
        ));
    }

    public record RecordRequest(
            Long endpointId,
            String status,
            Integer statusCode,
            Integer responseMs
    ) {}
}