# 05 Data Architecture

Version: 0.1  
Status: Draft

## Data Ownership Rules

- Each service owns its PostgreSQL schema or database.
- No service reads another service's database directly.
- Data duplication is allowed only through published events or owned API contracts.
- Analytics uses projections, not transactional database joins across services.

## Service Data Stores

| Service | PostgreSQL ownership | Notes |
|---|---|---|
| People Core | `people_db` | Worker identity, employment states, lifecycle events, personal fields |
| Organization | `org_db` | Teams, locations, cost centers, reporting relationships |
| Workflow | `workflow_db` | Templates, workflow instances, tasks, state transitions |
| Leave | `leave_db` | Policies, balances, requests, availability projections |
| Document | `document_db` | Metadata, classifications, retention policies, object references |
| Helpdesk | `helpdesk_db` | Tickets, comments, SLA state, assignments |
| Payroll Prep | `payroll_db` | Payroll periods, inputs, adjustments, approvals, exports |
| Developer Intelligence | `devintel_db` | Repositories, services, ownership, access requests, skill evidence |
| Risk Engine | `risk_db` | Risk rules, detections, scores, alert state |
| Notification | `notification_db` | Preferences, templates, delivery attempts |
| Audit | `audit_db` | Append-only audit records |
| Analytics | `analytics_db` | Cross-domain projections and dashboard aggregates |
| Integration | `integration_db` | Connector config, webhook checkpoints, provider mappings |

> Assumption: Separate logical databases are required for service autonomy; physical cluster sharing is allowed per environment and scale tier.

## Cross-Service Consistency

| Scenario | Pattern |
|---|---|
| Employee created then onboarding started | People Core publishes event; Workflow consumes |
| Role change affects access | People Core publishes event; Developer Intelligence creates access review tasks |
| Approved leave affects payroll prep | Leave publishes event; Payroll Prep updates period input |
| Contractor offboarding affects access and documents | Workflow saga coordinates tasks |
| Risk dashboard shows team availability + ownership | Analytics/Risk projections consume events |

## Reference Data Duplication

Services may store local snapshots of reference data:

- `worker_display_name`
- `manager_id`
- `team_id`
- `tenant_id`
- `employment_status`

Snapshots must include source event version and freshness timestamp.

## Sensitive Data Classes

| Data class | Examples | Controls |
|---|---|---|
| Public internal | Name, title, team | Tenant RBAC |
| Confidential HR | Compensation, performance, case notes | Field-level authorization, audit on read |
| Restricted identity | Government IDs, tax identifiers | Encryption, masking, strict purpose binding |
| Engineering access | Repo/cloud/production access | Security role checks, audit |
| Legal/compliance | Contracts, investigations | Legal hold, retention rules |

## Database Migrations

- Flyway is the default migration tool for Spring Boot services.
- Migrations are owned by the service team.
- Backward-compatible expand/contract changes are mandatory for zero-downtime deploys.
- Destructive migrations require architecture review and production data verification plan.

## Partitioning and Retention

| Store | Partitioning | Retention |
|---|---|---|
| Audit | Time + tenant hash | 7 years default |
| Events/outbox | Time | 30-180 days depending on event class |
| Notifications | Time | 1 year default |
| Helpdesk | Tenant + time | Configurable |
| Payroll Prep | Tenant + pay period | 7 years default, region-dependent |
| Risk history | Tenant + time | 2 years default |

## Read Models

Analytics projections:

- Org health dashboard.
- Workforce availability.
- Developer ownership risk.
- Payroll preparation summary.
- Helpdesk SLA board.
- Compliance evidence dashboard.

Projection rebuilds must be supported from retained Kafka topics or service-owned export APIs.
