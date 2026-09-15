# 10 Security and Compliance

Version: 0.1  
Status: Draft

## Security Model

stack-work-360 uses zero-trust principles:

- Authenticate every user and service.
- Authorize every request at resource and field level.
- Encrypt data in transit and at rest.
- Audit sensitive access.
- Minimize privilege by tenant, role, and purpose.

## Authentication

| Actor | Mechanism |
|---|---|
| Human users | OAuth2/OIDC through customer IdP or platform IdP |
| Services | mTLS workload identity |
| External integrations | OAuth app, signed webhook secret, or provider-specific secure token |
| Automation jobs | Service identity with scoped permissions |

## Authorization

Authorization layers:

1. Tenant membership.
2. Global platform role.
3. Tenant role.
4. Resource ownership or relationship.
5. Field-level permission.
6. Purpose-based access for restricted HR/legal/payroll data.

> Assumption: RBAC is required for MVP; ABAC/policy-as-code is introduced for complex enterprise authorization in Phase 2.

## Secrets Management

- Secrets stored in managed secrets provider.
- Short-lived credentials preferred.
- Rotation schedule defined by secret class.
- No secrets in logs, container images, Git, or build artifacts.
- Break-glass access requires approval and audit.

## Data Protection

| Control | Requirement |
|---|---|
| Encryption in transit | TLS externally, mTLS internally |
| Encryption at rest | Managed database/object storage encryption |
| Field encryption | Required for restricted identity and legal fields |
| Masking | Required for salary, tax, government, and legal data in UI where not needed |
| Data retention | Tenant-configurable within legal bounds |
| Data deletion | GDPR-ready deletion/anonymization workflow |

## Threat Model

| Threat | Mitigation |
|---|---|
| Cross-tenant data access | Mandatory tenant filters, authorization tests, tenant-aware indexes |
| Token theft | Short-lived tokens, refresh controls, device/session management |
| Privilege escalation | Central permission model, review workflows, audit |
| Malicious integration webhook | Signature validation, replay protection, rate limits |
| Kafka poison message | Schema validation, bounded retries, DLQ |
| Insider access to HR data | Least privilege, sensitive-read audit, break-glass |
| Data exfiltration through logs | Structured logging policy and automated scanning |
| Supply-chain attack | Dependency scanning, signed images, SBOM |

## Compliance Readiness

Initial control objectives:

- SOC 2 security, availability, and confidentiality.
- GDPR data subject rights.
- Audit evidence export.
- Access review evidence.
- Retention policies.
- Incident response process.

## Security Gates

Required before production:

- SAST.
- Dependency vulnerability scanning.
- Container image scanning.
- IaC scanning.
- Threat model review for new bounded contexts.
- Penetration test before GA.
- Secrets scanning in CI.

## Privacy Constraints

People-risk and developer-intelligence features must be explainable and must avoid raw productivity surveillance metrics.

Forbidden default metrics:

- Lines of code as performance score.
- Raw commit count as productivity score.
- Private message content ingestion.
- Always-on activity monitoring.
