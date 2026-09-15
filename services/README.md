# Backend Services

This folder contains independently deployable Spring Boot microservices.

## Rules

- A service owns its domain model, API, database migrations, and domain events.
- Services must not read another service's database.
- Synchronous calls must go through documented APIs with timeouts and resilience policies.
- Asynchronous communication must use Kafka events with idempotent consumers.
- Shared code belongs in `libs/` only when it is infrastructure-oriented and domain-neutral.

## Module Convention

```text
services/<service-name>/
  src/main/java/com/stackwork360/<servicename>/
  src/main/resources/application.yml
  src/test/java/com/stackwork360/<servicename>/
```

Add a new service by:

1. Creating the folder under `services/`.
2. Adding the module to `settings.gradle.kts`.
3. Creating a Spring Boot application entrypoint.
4. Adding service ownership metadata and board tasks.
