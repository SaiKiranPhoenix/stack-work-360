# 15 Roadmap and Milestones

Version: 0.1  
Status: Draft

## Roadmap Principles

- Build domain foundations before advanced automation.
- Release thin vertical slices across multiple services instead of isolated backend-only work.
- Keep MVP narrow enough for launch but architected for enterprise expansion.

## Phase 0: Foundation

Duration: 8-12 weeks

Milestones:

- Platform templates for Spring Boot and React.
- Kubernetes baseline.
- API Gateway baseline.
- OIDC integration.
- Kafka and Schema Registry baseline.
- PostgreSQL migration pipeline.
- Observability baseline.
- CI/CD pipeline templates.

Dependencies:

- Platform engineering.
- Security engineering.
- Frontend platform.

## Phase 1: MVP

Duration: 4-6 months

Milestones:

- People Core.
- Organization structure.
- Employee onboarding/offboarding workflow.
- Leave management.
- Document vault.
- HR helpdesk.
- Payroll preparation export.
- Developer access lifecycle.
- Engineering ownership map.
- Risk Engine v1.
- Audit and notification services.
- Role-specific React dashboards.

> Assumption: MVP launch supports a limited set of external integrations, likely one IdP, one Git provider, and one notification provider.

## Phase 2: Enterprise Workflow Expansion

Duration: 4-6 months after MVP

Milestones:

- Workflow builder.
- Workforce cost simulator.
- AI policy assistant.
- Manager copilot.
- Integration marketplace.
- Advanced analytics.
- ABAC/policy-as-code.
- Tenant-level configuration management.

## Phase 3: Global and Compliance Expansion

Duration: 6-9 months after Phase 2

Milestones:

- Global compliance packs.
- Data residency.
- Compensation benchmarking.
- Internal talent marketplace.
- Advanced skills graph.
- Enterprise audit exports.
- Regional failover.

## Phase 4: Platform Ecosystem

Milestones:

- Extension APIs.
- Customer workflow SDK.
- Partner connector framework.
- Active-active regional options for selected services.
- Marketplace for templates and integrations.

## Cross-Team Dependencies

| Dependency | Blocking teams |
|---|---|
| Auth and tenant context | All backend and frontend teams |
| Event envelope/schema standards | All event-producing teams |
| Design system | All frontend squads |
| Workflow engine | People Core, Developer Intelligence, Payroll, Documents |
| Audit service | All services with sensitive actions |
| Integration service | Developer Intelligence, People Core, Notifications |

## Effort Sizing

| Area | Relative effort |
|---|---|
| Platform foundation | XL |
| People Core + Org | L |
| Workflow engine | XL |
| Developer Intelligence | L |
| Risk Engine | L |
| Payroll Prep | M |
| Documents/compliance | M |
| Frontend dashboards | XL |
| Observability/security hardening | L |
