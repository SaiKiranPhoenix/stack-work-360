# 07 API Contracts and Standards

Version: 0.1  
Status: Draft

## API Styles

| API type | Standard use |
|---|---|
| REST | User-facing commands and queries through API Gateway |
| gRPC | Internal high-throughput service-to-service APIs when justified |
| Kafka | Domain events and asynchronous workflows |
| WebSocket/SSE | Live dashboard updates and workflow notifications |

> Assumption: REST is the default API style because the primary clients are React web and external enterprise integrations.

## REST Standards

- Base path: `/api/<domain>/v<major>/...`
- JSON request/response bodies.
- ISO-8601 UTC timestamps.
- UUIDs for public resource IDs unless domain requires otherwise.
- Cursor pagination for collections.
- Problem Details-style error responses.
- Idempotency key header for mutation endpoints.

Example headers:

```text
Authorization: Bearer <token>
X-Tenant-Id: <tenant-id>
X-Correlation-Id: <uuid>
Idempotency-Key: <uuid>
```

## Versioning Policy

| Change type | Versioning |
|---|---|
| Add optional response field | Same major |
| Add optional request field | Same major |
| Remove field | New major |
| Change field meaning/type | New major |
| Change authorization semantics | Architecture/security review |
| Change event schema incompatibly | New topic major |

## API Gateway Configuration

Gateway responsibilities:

- Route by path and API version.
- Enforce OIDC token presence and tenant context.
- Apply endpoint-class rate limits.
- Enforce request body size limits.
- Attach correlation IDs.
- Block deprecated API versions after sunset.
- Publish gateway access logs to observability pipeline.

Gateway must not contain business rules beyond coarse-grained route authorization.

## Contract Ownership

| Contract | Owner |
|---|---|
| People APIs | People Core team |
| Org APIs | Organization team |
| Workflow APIs | Workflow platform team |
| Leave APIs | Leave team |
| Payroll prep APIs | Payroll team |
| Developer intelligence APIs | Engineering Intelligence team |
| Risk APIs | Risk platform team |
| Event schemas | Producing service team with architecture review |
| Gateway route standards | Platform API team |

## Consumer-Driven Contracts

Required for:

- Service-to-service REST/gRPC.
- Public web API contracts consumed by multiple frontend squads.
- External integration APIs.

Each provider team publishes:

- OpenAPI or protobuf schema.
- Contract tests.
- Version compatibility matrix.
- Deprecation schedule.

## Error Model

Required fields:

```json
{
  "type": "https://docs.stack-work-360/errors/validation-error",
  "title": "Validation failed",
  "status": 400,
  "code": "VALIDATION_ERROR",
  "detail": "startDate must be before endDate",
  "correlationId": "uuid"
}
```

## Authorization Checks

Every backend endpoint must validate:

- Tenant membership.
- User or service identity.
- Role permission.
- Resource-level access.
- Field-level restrictions for sensitive HR, payroll, legal, and security data.
