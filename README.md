# Rushinga Provincial Disaster Monitoring and Management System (DPDMS)

This repository contains a working Spring Boot microservice starter for the assignment described in the brief. It is intentionally designed as a practical, group-ready foundation you can extend into a complete final submission.

## Project structure

- `services/hazard-core` — shared domain model, access policy, approval workflow, and in-memory store
- `services/discovery-service` — Eureka registry
- `services/gateway-service` — API gateway
- `services/flood-service` — flood incident microservice
- `services/drought-service` — drought incident microservice
- `services/fire-service` — fire incident microservice
- `services/zoonotic-disease-service` — zoonotic disease incident microservice
- `services/mining-accident-service` — mining accident incident microservice

## Why React?

React was chosen because it is excellent for a role-aware dashboard with interactive map markers, charts, filtering, and repeated UI patterns. It is flexible, easy to integrate with REST APIs, and widely used for dashboards similar to the one required in this project.

## How to run

1. Make sure you have Java 17 and Maven installed.
2. From the repository root, run:

```bash
mvn clean install
```

3. Start the services in this order:

```bash
mvn -pl services/discovery-service spring-boot:run
mvn -pl services/gateway-service spring-boot:run
mvn -pl services/flood-service spring-boot:run
mvn -pl services/drought-service spring-boot:run
mvn -pl services/fire-service spring-boot:run
mvn -pl services/zoonotic-disease-service spring-boot:run
mvn -pl services/mining-accident-service spring-boot:run
```

4. Use the API via the gateway or directly on each service port.

Default ports:

- discovery-service: 8761
- gateway-service: 8080
- flood-service: 8081
- drought-service: 8082
- fire-service: 8083
- zoonotic-disease-service: 8084
- mining-accident-service: 8085

## Example API request

Create a new flood incident:

```bash
curl -X POST http://localhost:8081/api/incidents \
  -H "Content-Type: application/json" \
  -H "X-Role: WARD_RECORDER" \
  -H "X-Hazard: FLOOD" \
  -H "X-Ward: Ward 1" \
  -d '{
    "ward":"Ward 1",
    "district":"Rushinga",
    "province":"Mashonaland Central",
    "occurredAt":"2026-09-25T10:00:00Z",
    "reporter":"recorder-1",
    "severity":"HIGH",
    "latitude":-16.75,
    "longitude":31.20,
    "hazardType":"FLOOD",
    "indicators": {
      "peakWaterLevelMetres": 3.2,
      "catchmentName": "Mazowe",
      "householdsDisplaced": 25,
      "areaFloodedHectares": 14.5,
      "inundationDurationDays": 2
    }
  }'
```

Approve the incident as a flood supervisor:

```bash
curl -X POST "http://localhost:8081/api/incidents/{id}/decision?status=APPROVED&reason=Validated" \
  -H "X-Role: PROVINCIAL_SUPERVISOR" \
  -H "X-Hazard: FLOOD" \
  -H "X-Ward: Ward 1"
```

## Security model

The project enforces the assignment rules in the backend:

- ward recorders can only work on one hazard in one ward
- provincial supervisors only approve their own hazard
- national users can read approved data across hazards but cannot write
- all writes are rejected with 403 when the request violates the hazard scope

## Notes for the final assignment

This starter includes the essential architecture, domain model, and workflow enforcement so the group can build a complete final deliverable. Each service is independent and deployable, and the design is intentionally simple enough to extend into a full production microservice system.

For the final full submission, the following items should be added:

- PostgreSQL schema and JPA repositories for each service
- JWT auth-service and Spring Security configuration
- report-service for PDF/DOCX/XLSX/CSV files
- alert-service and asynchronous email/WhatsApp integration
- dashboard-service and map UI
- integration tests for approval rules and hazard scoping
- OpenAPI docs, health checks, and audit persistence

## Step-by-step student guide

1. Use this repository as the base project.
2. Split responsibilities across the 5 group members: one hazard service each.
3. Implement the hazard-specific fields for each incident model.
4. Add JPA entities and repository classes for each service.
5. Add service-level approval workflow and authorization checks.
6. Add reaction logic for email/WhatsApp alerts.
7. Add front-end dashboard and map integration.
8. Add test cases for approval logic and hazard-level access control.
9. Commit regularly to GitHub and prepare the group presentation.

This project is intentionally modular and ready to be expanded into a full assignment solution.
