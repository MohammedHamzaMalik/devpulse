package com.hamza.devpulseapi.service;

import com.hamza.devpulseapi.exception.ResourceNotFoundException;
import com.hamza.devpulseapi.model.Endpoint;
import com.hamza.devpulseapi.model.HealthCheck;
import com.hamza.devpulseapi.repository.EndpointRepository;
import com.hamza.devpulseapi.repository.HealthCheckRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HealthCheckService {

    private final HealthCheckRepository healthCheckRepository;
    private final EndpointRepository endpointRepository;

    public HealthCheckService(HealthCheckRepository healthCheckRepository,
                              EndpointRepository endpointRepository) {
        this.healthCheckRepository = healthCheckRepository;
        this.endpointRepository = endpointRepository;
    }

    // Called by the Go monitor — no JWT, uses an internal API key instead
    public HealthCheck recordResult(Long endpointId, String status,
                                    Integer statusCode, Integer responseMs) {
        Endpoint endpoint = endpointRepository.findById(endpointId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Endpoint not found with id: " + endpointId));

        HealthCheck check = new HealthCheck(endpoint, status, statusCode, responseMs);
        return healthCheckRepository.save(check);
    }

    // Called by users querying their endpoint history
    public List<HealthCheck> getHistory(Long endpointId) {
        return healthCheckRepository
                .findTop20ByEndpointIdOrderByCheckedAtDesc(endpointId);
    }

    public Double getAvgResponseMs(Long endpointId) {
        return healthCheckRepository.findAvgResponseMs(endpointId);
    }
}