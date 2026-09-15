# 01 Requirements

Version: 0.1  
Status: Draft

## Functional Requirements

| ID | Requirement | MVP |
|---|---|---|
| F-001 | Maintain employee, contractor, team, role, manager, location, and lifecycle records | Yes |
| F-002 | Support onboarding workflows across HR, IT, engineering, and finance tasks | Yes |
| F-003 | Support role change, transfer, promotion, compensation-change, and offboarding workflows | Yes |
| F-004 | Manage leave requests, approvals, balances, calendars, and team availability | Yes |
| F-005 | Store controlled employee documents and policy acknowledgements | Yes |
| F-006 | Provide HR employee helpdesk with routing, SLA, comments, attachments, and audit trail | Yes |
| F-007 | Prepare payroll inputs from salary, leave, reimbursement, overtime, and contractor data | Yes |
| F-008 | Track developer access requests to repositories, CI/CD, cloud groups, and engineering tools | Yes |
| F-009 | Maintain engineering ownership map for repositories, services, APIs, and production systems | Yes |
| F-010 | Calculate people and engineering risk signals using event streams | Yes |
| F-011 | Provide org chart, skills graph, and workforce analytics dashboards | Yes |
| F-012 | Provide workforce cost simulator and scenario planning | Phase 2 |
| F-013 | Provide AI policy assistant and manager copilot | Phase 2 |
| F-014 | Support external HRIS/payroll/IdP/Git provider integrations | Phase 2 |
| F-015 | Support global employment compliance packs | Phase 3 |

> Assumption: MVP prioritizes workflow orchestration and system-of-record correctness over AI features.

## Non-Functional Requirements

| Category | Target |
|---|---|
| Availability | 99.9% for MVP, 99.95% for enterprise tier |
| API latency | p95 < 300 ms for read APIs, p95 < 700 ms for write APIs excluding workflow side effects |
| Dashboard freshness | Critical events visible within 5 seconds at p95 |
| Kafka processing lag | p95 event-to-consumer processing < 10 seconds for normal load |
| Scale | 10,000 tenants, 10 million worker profiles, 100,000 concurrent users |
| Data retention | Configurable by tenant and data class; audit logs retained 7 years by default |
| RPO | <= 15 minutes for core transactional data |
| RTO | <= 60 minutes for regional failover in enterprise tier |
| Security | Zero-trust service model, OIDC user auth, mTLS service auth |
| Compliance | SOC 2-ready controls, GDPR-ready data lifecycle, configurable retention |

> Assumption: Tenant scale targets are aspirational enterprise targets for architecture sizing, not MVP launch volume.

## MVP Scope

MVP includes:

- People Core.
- Organization and team structure.
- Onboarding/offboarding workflows.
- Leave and availability.
- Document vault.
- HR helpdesk.
- Payroll preparation export.
- Developer access lifecycle.
- Engineering ownership map.
- People Risk Engine v1.
- Audit logs and notifications.
- React web application for employee, manager, HR, finance, security, and engineering personas.

## Later Phases

| Phase | Capabilities |
|---|---|
| Phase 2 | Workflow builder, workforce cost simulator, AI policy assistant, manager copilot, integration marketplace |
| Phase 3 | Global compliance packs, compensation benchmarking, internal talent marketplace, advanced skills graph |
| Phase 4 | Multi-region active-active, large-enterprise data residency, extensibility SDK |

## Constraints

Mandated stack:

- ReactJS frontend.
- Spring Boot Java backend services.
- PostgreSQL database-per-service where appropriate.
- Apache Kafka backbone.
- Docker and Kubernetes.
- DDD-oriented microservices.

## Acceptance Criteria for Planning

- All service boundaries have clear owners.
- All cross-service data movement uses contracts.
- All writes are owned by exactly one service.
- All async consumers are idempotent.
- All user-facing APIs are exposed through the gateway.
- All sensitive data classes have protection and retention rules.
