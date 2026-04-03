package main

import (
	"bytes"
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"sync"
	"time"
)

const (
	apiBase    = "http://localhost:8080"
	monitorKey = "devpulse-monitor-secret-2024"
	interval   = 30 * time.Second
)

type Endpoint struct {
	ID  int    `json:"id"`
	URL string `json:"url"`
}

type HealthResult struct {
	EndpointID int    `json:"endpointId"`
	Status     string `json:"status"`
	StatusCode int    `json:"statusCode"`
	ResponseMs int64  `json:"responseMs"`
}

func fetchActiveEndpoints() ([]Endpoint, error) {
	req, _ := http.NewRequest("GET", apiBase+"/internal/endpoints", nil)
	req.Header.Set("X-Monitor-Key", monitorKey)

	client := &http.Client{Timeout: 10 * time.Second}
	resp, err := client.Do(req)
	if err != nil {
		return nil, fmt.Errorf("failed to fetch endpoints: %w", err)
	}
	defer resp.Body.Close()

	var endpoints []Endpoint
	if err := json.NewDecoder(resp.Body).Decode(&endpoints); err != nil {
		return nil, fmt.Errorf("failed to decode endpoints: %w", err)
	}
	return endpoints, nil
}

func checkEndpoint(endpoint Endpoint) HealthResult {
	client := &http.Client{Timeout: 10 * time.Second}

	start := time.Now()
	resp, err := client.Get(endpoint.URL)
	elapsed := time.Since(start).Milliseconds()

	if err != nil {
		return HealthResult{
			EndpointID: endpoint.ID,
			Status:     "DOWN",
			StatusCode: 0,
			ResponseMs: elapsed,
		}
	}
	defer resp.Body.Close()

	status := "UP"
	if resp.StatusCode >= 400 {
		status = "DOWN"
	}

	return HealthResult{
		EndpointID: endpoint.ID,
		Status:     status,
		StatusCode: resp.StatusCode,
		ResponseMs: elapsed,
	}
}

func recordResult(result HealthResult) error {
	body, _ := json.Marshal(result)
	resp, err := http.Post(
		apiBase+"/internal/health-checks",
		"application/json",
		bytes.NewBuffer(body),
	)
	if err != nil {
		return fmt.Errorf("failed to record result: %w", err)
	}
	defer resp.Body.Close()
	return nil
}

func runChecks() {
	endpoints, err := fetchActiveEndpoints()
	if err != nil {
		log.Printf("ERROR fetching endpoints: %v", err)
		return
	}

	if len(endpoints) == 0 {
		log.Println("No active endpoints to monitor")
		return
	}

	log.Printf("Checking %d endpoints...", len(endpoints))

	var wg sync.WaitGroup
	for _, endpoint := range endpoints {
		wg.Add(1)
		go func(ep Endpoint) {
			defer wg.Done()

			result := checkEndpoint(ep)

			err := recordResult(result)
			if err != nil {
				log.Printf("ERROR recording result for %s: %v", ep.URL, err)
				return
			}

			log.Printf("[%s] %s — %dms (HTTP %d)",
				result.Status, ep.URL, result.ResponseMs, result.StatusCode)
		}(endpoint)
	}
	wg.Wait()
}

func main() {
	log.Println("DevPulse Monitor starting...")
	log.Printf("Checking every %s", interval)

	// Run immediately on startup
	runChecks()

	// Then run on a ticker
	ticker := time.NewTicker(interval)
	defer ticker.Stop()

	for range ticker.C {
		runChecks()
	}
}
