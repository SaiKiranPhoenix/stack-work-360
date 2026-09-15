# 12 Tech Stack Decisions

Version: 0.1  
Status: Draft ADR set

## ADR-001 Frontend: ReactJS

Status: Accepted  
Decision: Use ReactJS for the web frontend.

Alternatives considered:

- Angular.
- Vue.
- Server-rendered templates.

Rationale:

- Large hiring pool.
- Strong ecosystem for enterprise dashboards.
- Works well with design systems and role-specific app shells.
- Mandated stack requirement.

Trade-offs:

- Requires strong frontend standards to avoid inconsistent patterns across teams.

## ADR-002 Backend: Spring Boot Java

Status: Accepted  
Decision: Use Spring Boot for backend microservices.

Alternatives considered:

- Node.js/NestJS.
- Go.
- .NET.

Rationale:

- Mature enterprise ecosystem.
- Strong observability, security, and data access support.
- Good fit for large multi-team organizations.
- Mandated stack requirement.

## ADR-003 Persistence: PostgreSQL Per Service

Status: Accepted  
Decision: PostgreSQL is the primary persistent database; services own their schemas/databases.

Alternatives considered:

- MySQL.
- MongoDB.
- Shared enterprise database.

Rationale:

- Strong relational integrity.
- Good JSON and indexing support.
- Mature operational tooling.
- Database-per-service supports bounded-context autonomy.

Trade-offs:

- Cross-service reporting requires projections.
- Distributed joins are not allowed.

## ADR-004 Messaging: Apache Kafka

Status: Accepted  
Decision: Kafka is the event backbone.

Alternatives considered:

- RabbitMQ.
- Cloud pub/sub only.
- SQS/SNS-style queues.

Rationale:

- High-throughput durable event streams.
- Consumer-group scaling.
- Replayable topics for projections.
- Fits event-driven workflows and analytics.

Trade-offs:

- Requires disciplined schema governance and consumer idempotency.

## ADR-005 Architecture: DDD Microservices

Status: Accepted  
Decision: Decompose by bounded context.

Alternatives considered:

- Modular monolith.
- Shared service database.
- Function-per-feature serverless.

Rationale:

- Required organizational scale demands parallel ownership.
- Domain boundaries reduce coordination load.
- Enables independent deployment and scaling.

Trade-offs:

- Higher operational complexity.
- Requires strong platform engineering and governance.

## ADR-006 Containers and Kubernetes

Status: Accepted  
Decision: Use Docker images orchestrated by Kubernetes.

Alternatives considered:

- VM-based deployment.
- PaaS-only deployment.
- Serverless-only deployment.

Rationale:

- Standardized deployment across many services.
- Autoscaling and rollout controls.
- Broad ecosystem support.

## ADR-007 Observability: OpenTelemetry

Status: Proposed  
Decision: Use OpenTelemetry for traces, metrics, and logs correlation.

Rationale:

- Vendor-neutral.
- Supports distributed tracing across Spring Boot, Kafka, gateway, and frontend.

## ADR-008 Security: OIDC + mTLS

Status: Accepted  
Decision: Use OAuth2/OIDC for users and mTLS for service-to-service identity.

Rationale:

- Fits enterprise SSO.
- Supports zero-trust service architecture.
- Aligns with compliance requirements.
