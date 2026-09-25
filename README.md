# Rushinga Provincial Disaster Monitoring and Management System (DPDMS)

A Spring Boot microservices foundation for the HCS201/HCC201/HAI201 Object-Oriented Programming assignment at the University of Zimbabwe.

## Architecture

The repository is organised as separately deployable services:

- `discovery-service` — Eureka service registry
- `gateway-service` — single client entry point
- `hazard-core` — reusable domain, validation, approval, and hazard-scoping logic
- `flood-service`
- `drought-service`
- `fire-service`
- `zoonotic-disease-service`
- `mining-accident-service`

Each hazard service has its own Spring Boot application and port. The service-specific configuration assigns a separate PostgreSQL schema for production deployment. The current core implementation uses an in-memory repository so the API and workflow can be run immediately; the repository boundary is intentionally isolated for replacement with Spring Data JPA/PostgreSQL.

The front end will use **React** because it supports a reusable dashboard, interactive maps, and role-aware views with a large ecosystem of mapping and charting libraries. Services communicate over HTTP through the gateway and are discoverable through Eureka.

## Run locally

Requirements: Java 17, Maven 3.9+, and Docker (optional).

```bash
mvn clean verify
mvn -pl services/flood-service spring-boot:run
```

Run the other hazard services by changing the module path. The default ports are 8081–8085. Eureka runs on 8761 and the gateway on 8080.

```bash
docker compose up -d postgres
```

## API examples

Create a flood as a ward recorder:

```bash
curl -X POST http://localhost:8081/api/incidents \
  -H 'Content-Type: application/json' \
  -H 'X-Role: WARD_RECORDER' \
  -H 'X-Hazard: FLOOD' \
  -H 'X-Ward: Ward 1' \
  -d '{"ward":"Ward 1","district":"Rushinga","province":"Mashonaland Central","occurredAt":"2026-09-25T10:00:00Z","reporter":"recorder-1","severity":"HIGH","latitude":-16.7,"longitude":31.2,"indicators":{"peakWaterLevelMetres":3.2,"catchmentName":"Mazowe","householdsDisplaced":25,"areaFloodedHectares":14.5,"inundationDurationDays":2}}'
```

All new incidents start as `PENDING`. Only the hazard-specific supervisor can approve, reject, or request corrections. Only approved incidents are returned to ordinary read queries. National users can read approved incidents across hazards but all write operations are rejected.

## Security and production work

The policy and service boundary are implemented in `hazard-core`. Production deployment must replace the development identity headers with a signed JWT issued by `auth-service`, enable Spring Security resource-server validation, use PostgreSQL per service, and publish approval/alert events through RabbitMQ. Secrets for email and WhatsApp providers must be supplied through environment variables. OpenAPI, Actuator, audit persistence, report generation, alerting, React dashboard, and integration tests are planned as the next modules in the repository.
