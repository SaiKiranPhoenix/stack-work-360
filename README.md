# stack-work-360

Enterprise monorepo for stack-work-360: a People Operations and Engineering Workforce Intelligence platform built with React, Spring Boot microservices, PostgreSQL, Kafka, Docker, and Kubernetes.

## Repository Layout

| Path | Purpose |
|---|---|
| `apps/web` | Modular React frontend |
| `services/*` | Independently deployable Spring Boot microservices |
| `libs/common` | Shared backend primitives that do not contain domain logic |
| `libs/events` | Event envelope and messaging contracts |
| `libs/security` | Security context and authorization helper contracts |
| `libs/web` | Web/API response and request helpers |
| `infra` | Local and deployment infrastructure assets |
| `planning` | Architecture and product planning documents |
| `PRODUCT_COMPLETION_BOARD.md` | Living execution tracker |

## Backend Principles

- Each service owns its API, database migrations, domain model, and events.
- Shared libraries must stay infrastructure-oriented; domain logic belongs inside services.
- Services communicate through REST/gRPC for synchronous needs and Kafka for domain events.
- Every service is independently buildable and deployable from the monorepo.

## Frontend Principles

- `apps/web/src/app` owns app bootstrap and routing composition.
- `apps/web/src/features` owns domain feature modules.
- `apps/web/src/shared` owns reusable UI, API, and cross-feature utilities.
- Features should not import from each other directly; shared abstractions move to `shared`.

## Local Start

The scaffold is intentionally dependency-light. After Gradle and Node tooling are installed:

```powershell
gradle clean build
cd apps/web
npm install
npm run dev
```

See `planning/` for architecture direction and `PRODUCT_COMPLETION_BOARD.md` for execution tracking.
