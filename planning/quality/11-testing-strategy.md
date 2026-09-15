# 11 Testing Strategy

Version: 0.1  
Status: Draft

## Testing Pyramid

| Test type | Purpose | Owner |
|---|---|---|
| Unit | Domain logic, validation, mapping | Service team |
| Component | Service with local dependencies/mocks | Service team |
| Integration | PostgreSQL, Kafka, cache, object storage interactions | Service team |
| Contract | Provider/consumer compatibility | Provider + consumer teams |
| End-to-end | Critical user journeys | Quality engineering |
| Performance | Load, soak, spike, Kafka lag | Performance team |
| Security | AuthZ, injection, dependency, tenant isolation | Security engineering |
| Chaos | Resilience under dependency failure | Platform + service teams |

## Coverage Targets

| Area | Target |
|---|---|
| Critical domain logic | 90%+ meaningful branch coverage |
| Service API controllers | Contract-backed coverage |
| Workflow state machines | 100% transition coverage |
| Authorization policies | 100% rule coverage |
| Event consumers | Success, duplicate, out-of-order, invalid, and DLQ paths |

> Assumption: Coverage percentages are quality gates for critical modules, not blanket vanity metrics across generated code.

## Consumer-Driven Contract Testing

Required for:

- API Gateway to backend services.
- Frontend BFF/API contracts.
- Service-to-service REST/gRPC.
- Event schema compatibility.

Contract tests run before provider deployment and during consumer pipeline validation.

## Test Data

- Synthetic tenant fixtures.
- Golden lifecycle scenarios.
- Large tenant load fixtures.
- Privacy-safe anonymized staging datasets where legally approved.
- No production data in local development.

## Critical E2E Journeys

1. Hire worker -> onboarding tasks -> access request -> first-week completion.
2. Employee requests leave -> manager approves -> payroll input updates.
3. Role change -> access review -> permission update -> audit evidence.
4. Resignation -> offboarding workflow -> final payroll prep -> access removal.
5. Git service ownership change -> bus-factor risk recalculated.
6. HR ticket created -> routed -> SLA escalated -> closed.

## Kafka Testing

Each consumer test suite must include:

- Duplicate event.
- Missing optional field.
- Unknown future field.
- Stale event version.
- Poison message.
- Retryable downstream failure.
- Non-retryable validation failure.

## Chaos Testing

Minimum scenarios:

- Kill one service pod during workflow execution.
- Delay Kafka consumers.
- Disable external Git provider.
- Fail PostgreSQL read replica.
- Inject high gateway latency.
- Drop notification provider calls.
- Force outbox relay pause.

## Release Qualification

Production release requires:

- Unit/component tests pass.
- Contract tests pass.
- Database migration validated.
- Security scans pass or approved exception exists.
- Canary health checks pass.
- Rollback plan documented.
