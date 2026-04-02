package com.hamza.devpulseapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "health_checks")
public class HealthCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "endpoint_id")
    private Endpoint endpoint;

    private String status;
    private Integer statusCode;
    private Integer responseMs;
    private LocalDateTime checkedAt;

    public HealthCheck() {}

    public HealthCheck(Endpoint endpoint, String status, Integer statusCode, Integer responseMs) {
        this.endpoint = endpoint;
        this.status = status;
        this.statusCode = statusCode;
        this.responseMs = responseMs;
        this.checkedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Endpoint getEndpoint() { return endpoint; }
    public String getStatus() { return status; }
    public Integer getStatusCode() { return statusCode; }
    public Integer getResponseMs() { return responseMs; }
    public LocalDateTime getCheckedAt() { return checkedAt; }
}
