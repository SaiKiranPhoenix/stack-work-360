# 09 Observability Plan

Version: 0.1  
Status: Draft

## Objectives

Observability must support:

- Customer-facing incident response.
- Cross-service debugging.
- Kafka lag and event failure diagnosis.
- SLO reporting.
- Audit and security investigations.
- Capacity planning.

## Telemetry Standards

| Signal | Standard |
|---|---|
| Logs | Structured JSON logs |
| Metrics | OpenTelemetry metrics exported to metrics backend |
| Traces | OpenTelemetry distributed tracing |
| Events | Kafka event metadata correlated with traces |
| Frontend | Browser performance and client errors |

Required correlation fields:

- `trace_id`
- `correlation_id`
- `tenant_id`
- `service_name`
- `service_version`
- `actor_id` when available
- `event_id` when event-driven

## SLOs

| Service area | SLO |
|---|---|
| API Gateway | 99.95% availability, p95 latency < 150 ms excluding backend |
| People Core | 99.9% availability, p95 read < 250 ms, p95 write < 500 ms |
| Workflow | 99.9% availability, p95 command < 700 ms |
| Leave | 99.9% availability, p95 read < 300 ms |
| Payroll Prep | 99.9% availability during payroll windows |
| Developer Intelligence | 99.5% provider-sync availability |
| Risk Engine | 95% of risk signals generated within 60 seconds of source event |
| Notification | 99% delivery handoff within 30 seconds |

> Assumption: SLOs are platform-level initial targets and require product/legal validation before inclusion in customer contracts.

## Dashboards

Required dashboards:

- Executive platform health.
- Service golden signals.
- Kafka topic and consumer lag.
- Database health per service.
- API Gateway traffic and errors.
- Workflow failure and SLA board.
- Risk Engine freshness and alert volume.
- Tenant-level noisy-neighbor dashboard.
- Frontend performance.

## Alerting

Alert only on actionable conditions:

- SLO burn rate.
- Kafka consumer lag above threshold.
- DLQ volume above threshold.
- Database replication lag.
- Error-rate spike.
- p95/p99 latency spike.
- Outbox relay stuck.
- Failed offboarding/access removal workflows.
- Payroll period close failures.

## Logging Rules

- Do not log secrets, tokens, government IDs, salary amounts, or raw document contents.
- Sensitive IDs must be tokenized or masked.
- Logs must include enough context for debugging without exposing restricted data.
- Security-relevant actions produce audit events, not only logs.

## Trace Requirements

Every service must trace:

- Incoming requests.
- Outgoing HTTP/gRPC calls.
- Database operations at summarized level.
- Kafka publish and consume operations.
- Workflow task transitions.

## Runbooks

Each production service must publish runbooks for:

- High error rate.
- High latency.
- Kafka lag.
- DLQ growth.
- Database failover.
- External provider outage.
- Data correction procedure.
