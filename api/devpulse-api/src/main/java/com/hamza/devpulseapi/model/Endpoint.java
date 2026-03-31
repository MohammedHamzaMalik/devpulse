package com.hamza.devpulseapi.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "endpoints")
public class Endpoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String url;
    private String ownerEmail;
    private Boolean active;
    private LocalDateTime createdAt;

    public Endpoint(){}

    public Endpoint(String name, String url, String ownerEmail){
        this.name=name;
        this.url=url;
        this.ownerEmail=ownerEmail;
        this.active=true;
        this.createdAt=LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getUrl() {return url; }
    public String getOwnerEmail() { return ownerEmail; }
    public Boolean getActive() { return active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setActive(Boolean active) { this.active = active; }
}
