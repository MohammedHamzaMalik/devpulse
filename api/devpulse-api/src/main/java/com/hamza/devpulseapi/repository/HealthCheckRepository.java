package com.hamza.devpulseapi.repository;

import com.hamza.devpulseapi.model.HealthCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HealthCheckRepository extends JpaRepository<HealthCheck, Long> {
    List<HealthCheck> findTop20ByEndpointIdOrderByCheckedAtDesc(Long endpointId);

    @Query("SELECT AVG(h.responseMs) FROM HealthCheck h WHERE h.endpoint.id = :endpointId")
    Double findAvgResponseMs(Long endpointId);
}
