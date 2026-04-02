package com.hamza.devpulseapi.service;

import com.hamza.devpulseapi.exception.ResourceNotFoundException;
import com.hamza.devpulseapi.model.Endpoint;
import com.hamza.devpulseapi.repository.EndpointRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EndpointService {
    private final EndpointRepository repository;
    public EndpointService(EndpointRepository repository){
        this.repository=repository;
    }

    private String currentUser(){
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }

    public List<Endpoint> getMyEndpoints(){
        return repository.findByOwnerEmailAndActiveTrue(currentUser());
    }

    public Endpoint register(String name, String url){
        Endpoint endpoint = new Endpoint(name,url,currentUser());
        return repository.save(endpoint);
    }

    public Endpoint getById(Long id){
        Endpoint endpoint = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Endpoint not found with id: "+id));
        if (!endpoint.getOwnerEmail().equals(currentUser())){
            throw new ResourceNotFoundException("Endpoint not found with id: "+id);
        }
        return endpoint;
    }

    public void deactivate(Long id){
        Endpoint endpoint = getById(id);
        endpoint.setActive(false);
        repository.save(endpoint);
    }
}
