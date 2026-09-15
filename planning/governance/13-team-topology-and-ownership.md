# 13 Team Topology and Ownership

Version: 0.1  
Status: Draft

## Team Model

> Assumption: The organization uses stream-aligned product teams, enabling platform teams, and governance forums rather than a centralized architecture bottleneck.

## Team Map

| Team | Owns |
|---|---|
| People Core Team | People Core Service, employee lifecycle APIs |
| Organization Team | Org Service, org chart, cost centers, locations |
| Workflow Platform Team | Workflow Service, approval engine, workflow templates |
| Leave & Availability Team | Leave Service, availability projections |
| Documents & Compliance Team | Document Service, policy acknowledgements, retention |
| Helpdesk Team | Helpdesk Service, SLA engine |
| Payroll Prep Team | Payroll Prep Service, pay-cycle workflows |
| Engineering Intelligence Team | Developer Intelligence Service, access lifecycle, ownership map |
| Risk Platform Team | Risk Engine, risk rules, signal explanations |
| Notifications Team | Notification Service and delivery providers |
| Integrations Team | External connectors and normalization |
| Analytics Team | Analytics projections and dashboards |
| Platform Engineering | Kubernetes, CI/CD, golden templates, service mesh |
| Security Engineering | Auth, authorization standards, threat modeling, security gates |
| Frontend Platform | React shell, design system, frontend build pipeline |
| API Governance | API standards, gateway policy, contract review |

## RACI

| Decision | Responsible | Accountable | Consulted | Informed |
|---|---|---|---|---|
| New service creation | Domain team | Architecture council | Platform, security | All engineering |
| Event schema major version | Producer team | API governance | Consumers, architecture | All affected teams |
| Database migration standard | Service team | Platform engineering | DBA/data architecture | Domain teams |
| Authorization model change | Security engineering | CISO/security lead | Domain teams | All engineering |
| SLO change | Service team | Product + SRE | Support, architecture | Customers where contractual |
| Production incident | On-call team | Incident commander | Affected teams | Stakeholders |
| Tech stack exception | Requesting team | Architecture council | Platform/security | Engineering leadership |

## Ownership Rules

- Every service has one owning team.
- Every Kafka topic has one producing owner.
- Every API contract has one owning team.
- Shared libraries require named maintainers.
- No orphan services or topics are allowed in production.

## Architecture Governance

Forums:

- Weekly architecture review board.
- API and event schema review.
- Security design review.
- Operational readiness review.
- Quarterly domain-boundary review.

Architecture review is required for:

- New service.
- New persistent store.
- New external integration category.
- New sensitive data class.
- New synchronous dependency between services.
- New cross-region replication model.
