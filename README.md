# DevPulse — API Health Monitoring Platform

A real-time API monitoring system built with Java Spring Boot and Go.
Register your API endpoints and DevPulse automatically checks their
health every 30 seconds, recording response times and status history.

## Architecture
```
┌─────────────────┐         ┌──────────────────┐
│   Java API      │         │   Go Monitor     │
│  Spring Boot    │◄────────│                  │
│  Port 8080      │         │  Goroutines ping │
│                 │────────►│  each endpoint   │
│  User mgmt      │         │  concurrently    │
│  Endpoint mgmt  │         │  every 30s       │
│  History API    │         └──────────────────┘
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│   PostgreSQL    │
│   Port 5432     │
└─────────────────┘
```

## Why two languages?

The Go monitor runs one tight loop making thousands of
lightweight HTTP calls. Go goroutines handle this concurrency
cheaper than Java threads. The Java API handles user management
and data serving where Spring Boot's ecosystem saves significant
development time.

## Quick start
```bash
docker-compose up --build
```

## API Reference

### Auth
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/auth/register` | Register a user |
| POST | `/auth/login` | Login, get JWT token |

### Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/endpoints` | List your endpoints |
| POST | `/api/endpoints` | Register an endpoint |
| DELETE | `/api/endpoints/{id}` | Deactivate an endpoint |
| GET | `/api/endpoints/{id}/history` | Last 20 health checks |
| GET | `/api/endpoints/{id}/stats` | Average response time |

## Tech stack
- Java 17 + Spring Boot — management API
- Go 1.22 — concurrent health monitor
- PostgreSQL 16 — persistence
- Docker Compose — orchestration
- JWT — authentication
- BCrypt — password hashing