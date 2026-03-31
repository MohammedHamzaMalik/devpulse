package com.hamza.devpulseapi.repository;

import com.hamza.devpulseapi.model.Endpoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EndpointRepository extends JpaRepository<Endpoint, Long> {
    List<Endpoint> findByOwnerEmailAndActiveTrue(String ownerEmail);
}
