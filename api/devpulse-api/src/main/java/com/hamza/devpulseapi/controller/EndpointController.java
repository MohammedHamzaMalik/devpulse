package com.hamza.devpulseapi.controller;

import com.hamza.devpulseapi.model.Endpoint;
import com.hamza.devpulseapi.service.EndpointService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/endpoints")
public class EndpointController {
    private final EndpointService service;
    public EndpointController(EndpointService service){
        this.service=service;
    }

    @GetMapping
    public List<Endpoint> getMyEndpoints(){
        return service.getMyEndpoints();
    }

    @PostMapping
    public ResponseEntity<Endpoint> register(
            @Valid @RequestBody RegisterRequest request){
        Endpoint created = service.register(request.name(),request.url());
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping("/{id}")
    public Endpoint getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable Long id){
        service.deactivate(id);
        return ResponseEntity.noContent().build();
    }

    public record RegisterRequest(
            @NotBlank(message = "Name is required")
            String name,

            @NotBlank(message = "URL is required")
            @Pattern(
                    regexp = "^https?://.*",
                    message = "URL must start with http:// or https://"
            )
            String url
    ) {}
}
