# 16 Glossary and Open Questions

Version: 0.1  
Status: Draft

## Glossary

| Term | Meaning |
|---|---|
| Worker | Any employee, contractor, intern, vendor user, or external collaborator represented in the platform |
| Employee lifecycle | Hire, onboard, manage, transfer, promote, compensate, leave, offboard |
| People Core | Source of truth for worker identity, employment state, reporting lines, and organization structure |
| Bounded context | A domain boundary with its own model, language, data ownership, and service ownership |
| Workflow | A governed sequence of tasks, approvals, events, and side effects |
| People Risk | Risk derived from people, access, ownership, workload, compliance, or lifecycle signals |
| Engineering ownership | Relationship between people, teams, repositories, services, APIs, incidents, and deployment systems |
| Bus factor | Concentration risk where too few people understand or own a critical system |
| Payroll preparation | Calculation and approval of payroll inputs before sending to a payroll provider |
| Tenant | Customer organization using stack-work-360 |
| Control plane | Platform components used for tenancy, configuration, routing, policy, and governance |
| Data plane | Tenant business data and workload-serving services |

## Open Questions

| ID | Question | Owner | Impact |
|---|---|---|---|
| OQ-001 | Confirm product concept and target market from [00-product-overview.md](00-product-overview.md). | Product leadership | Critical |
| OQ-002 | Should stack-work-360 become a payroll processor later or remain payroll-preparation only? | Product/legal | High |
| OQ-003 | Which identity providers are required for launch: Okta, Entra ID, Google Workspace, others? | Security/product | High |
| OQ-004 | Which Git providers are required for developer intelligence: GitHub, GitLab, Bitbucket, Azure DevOps? | Engineering platform | Medium |
| OQ-005 | Is tenant data residency required in MVP? | Sales/legal | High |
| OQ-006 | Should analytics use a separate warehouse in Phase 1 or start with service read models only? | Data architecture | Medium |
| OQ-007 | What legal constraints apply to people-risk scoring in target regions? | Legal/privacy | Critical |
| OQ-008 | Which compliance frameworks are launch requirements: SOC 2, ISO 27001, GDPR, HIPAA-adjacent controls? | GRC | High |
| OQ-009 | Will customers allow ingestion of engineering metadata for risk analysis? | Product/security | High |
| OQ-010 | Should the platform offer extension APIs at launch? | Platform architecture | Medium |

## Assumption Register

This file tracks unresolved decisions only. Confirmed assumptions should be promoted into owned architecture docs through the governance process in [12-tech-stack-decisions.md](governance/12-tech-stack-decisions.md).
