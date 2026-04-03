package com.hamza.devpulseapi.controller;

import com.hamza.devpulseapi.model.Endpoint;
import com.hamza.devpulseapi.repository.EndpointRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/internal")
public class MonitorController {
    private static final String MONITOR_KEY = "devpulse-monitor-secret-2024";
    private final EndpointRepository endpointRepository;

    public MonitorController(EndpointRepository endpointRepository) {
        this.endpointRepository = endpointRepository;
    }

    @GetMapping("/endpoints")
    public ResponseEntity<?> getActiveEndpoints(
            @RequestHeader("X-Monitor-Key") String key) {
        if (!MONITOR_KEY.equals(key)){
            return ResponseEntity.status(401)
                    .body("Invalid monitor key");
        }

        List<Endpoint> endpoints = endpointRepository.findAllActive();
        return ResponseEntity.ok(endpoints);
    }
}
