# 06 Event-Driven Architecture

Version: 0.1  
Status: Draft

## Kafka Principles

- Kafka is the backbone for domain events, workflow triggers, audit enrichment, projections, and integrations.
- Events are immutable facts.
- Commands are not sent over shared domain topics.
- Every event has a schema in Schema Registry.
- Consumers are idempotent and tolerate reordering where possible.

## Topic Naming

Pattern:

```text
<environment>.<domain>.<event-stream>.v<major>
```

Example:

```text
prod.people.worker-lifecycle.v1
```

## Core Topics

| Topic | Producer | Key | Consumers |
|---|---|---|---|
| `*.people.worker-lifecycle.v1` | People Core | `tenant_id:worker_id` | Workflow, Payroll Prep, Developer Intelligence, Audit, Analytics |
| `*.org.structure.v1` | Organization | `tenant_id:org_unit_id` | Analytics, Risk, Developer Intelligence |
| `*.leave.request.v1` | Leave | `tenant_id:worker_id` | Payroll Prep, Risk, Analytics, Notification |
| `*.workflow.instance.v1` | Workflow | `tenant_id:workflow_id` | Audit, Notification, Analytics |
| `*.helpdesk.ticket.v1` | Helpdesk | `tenant_id:ticket_id` | Risk, Analytics, Notification |
| `*.payroll.period.v1` | Payroll Prep | `tenant_id:payroll_period_id` | Audit, Analytics, Notification |
| `*.devintel.ownership.v1` | Developer Intelligence | `tenant_id:service_id` | Risk, Analytics, Audit |
| `*.devintel.access.v1` | Developer Intelligence | `tenant_id:access_request_id` | Workflow, Audit, Notification |
| `*.risk.signal.v1` | Risk Engine | `tenant_id:risk_signal_id` | Notification, Analytics, Workflow |
| `*.audit.activity.v1` | Services | `tenant_id:resource_id` | Audit |

> Assumption: Kafka message payloads use Avro or Protobuf with a central schema registry; final serialization format must be standardized before service teams begin implementation.

## Event Envelope

Required fields:

```json
{
  "event_id": "uuid",
  "event_type": "employee.offboarding.started",
  "event_version": 1,
  "occurred_at": "2026-09-15T00:00:00Z",
  "producer": "people-core-service",
  "tenant_id": "tenant-id",
  "actor": {
    "type": "user|service|system",
    "id": "actor-id"
  },
  "correlation_id": "uuid",
  "trace_id": "trace-id",
  "payload": {}
}
```

## Partitioning Strategy

| Stream | Partition key | Rationale |
|---|---|---|
| Worker lifecycle | `tenant_id:worker_id` | Preserve per-worker ordering |
| Org structure | `tenant_id:org_unit_id` | Preserve hierarchy change ordering |
| Workflow | `tenant_id:workflow_id` | Preserve workflow state order |
| Access request | `tenant_id:access_request_id` | Preserve approval state order |
| Risk signal | `tenant_id:risk_signal_id` | Preserve alert lifecycle order |
| Audit | `tenant_id:resource_id` | Balanced audit distribution |

High-volume tenants may receive dedicated topics or partition allocations after platform review.

## Consumer Groups

Each service owns consumer group names:

```text
<service-name>.<purpose>.<environment>
```

Examples:

- `risk-engine.worker-risk.prod`
- `analytics.org-projection.prod`
- `notification.delivery.prod`

## Schema Evolution

Rules:

- Backward-compatible changes only within a major version.
- Required field removal requires new major topic version.
- Consumers must ignore unknown fields.
- Producers must not reuse field names with changed meaning.
- Schema compatibility checks run in CI.

## Dead-Letter Queues

Pattern:

```text
<source-topic>.dlq
```

DLQ record includes:

- Original event.
- Failure reason.
- Consumer group.
- Attempt count.
- Stack trace reference.
- First failure time.
- Last failure time.

DLQ replay requires runbook approval for production.

## Transactional Outbox

Services that write PostgreSQL and publish Kafka events must use transactional outbox:

1. Write domain state and outbox row in one database transaction.
2. Outbox relay publishes to Kafka.
3. Relay marks outbox row published.
4. Consumer deduplicates by `event_id`.

## Exactly-Once vs At-Least-Once

Default is at-least-once delivery with idempotent consumers. Exactly-once processing may be used for Kafka-to-Kafka stream transformations, but not as a substitute for business idempotency.
