# 14 Engineering Standards

Version: 0.1  
Status: Draft

## General Standards

- Services must follow the golden Spring Boot template.
- Frontend modules must use the approved React app shell and design system.
- Every service must expose health, readiness, metrics, and OpenAPI endpoints.
- Every production service must have runbooks.
- Every service must define ownership metadata.

## Java/Spring Standards

- Java LTS version standardized by platform team.
- Constructor injection.
- Explicit transaction boundaries.
- Validation at API and domain boundaries.
- No direct database access outside owning service.
- Resilience policies on all outbound calls.
- Structured logging with correlation fields.

## React Standards

- TypeScript recommended for all production React code.
- Shared design system components for common UI.
- Role-based route guards.
- Query caching through approved data-fetching library.
- Accessibility checks in CI.
- No sensitive data persisted in browser local storage.

> Assumption: TypeScript is permitted for React code even though the mandated stack names ReactJS.

## Code Review

Required:

- At least two approvals for production code.
- Security approval for sensitive-data or auth changes.
- Architecture approval for service-boundary changes.
- Contract tests updated with API/event changes.
- Migration and rollback plan for database changes.

## Branching Strategy

Default:

- Trunk-based development.
- Short-lived feature branches.
- Feature flags for incomplete work.
- Release branches only for supported enterprise versions if needed.

## Dependency Policy

- Platform team publishes approved BOMs and base images.
- Teams may request exceptions through architecture review.
- Critical vulnerabilities require patch plan within SLA.
- Unmaintained libraries are prohibited.
- Shared libraries must preserve backward compatibility or publish migration guide.

## API and Event Standards

- Follow [../api/07-api-contracts-and-standards.md](../api/07-api-contracts-and-standards.md).
- All events use schema registry.
- All mutations support idempotency where clients may retry.
- All APIs include correlation ID support.

## Definition of Done

A production feature is done when:

- Code merged and deployed.
- Tests pass.
- Contracts updated.
- Observability added.
- Authorization verified.
- Docs updated.
- Runbook updated if operational behavior changed.
- Feature flag and rollout plan defined.
