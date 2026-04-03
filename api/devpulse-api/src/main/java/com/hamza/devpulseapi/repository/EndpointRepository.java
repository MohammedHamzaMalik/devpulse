package com.hamza.devpulseapi.repository;

import com.hamza.devpulseapi.model.Endpoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EndpointRepository extends JpaRepository<Endpoint, Long> {
    List<Endpoint> findByOwnerEmailAndActiveTrue(String ownerEmail);

    @Query("SELECT e FROM Endpoint e WHERE e.active = true")
    List<Endpoint> findAllActive();
}
