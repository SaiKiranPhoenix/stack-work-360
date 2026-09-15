# Backend Shared Libraries

Shared libraries exist to keep service scaffolding consistent without centralizing domain logic.

| Library | Purpose |
|---|---|
| `common` | Tenant IDs, correlation constants, small platform primitives |
| `events` | Event envelope and messaging contracts |
| `security` | Actor context and authorization helper contracts |
| `web` | API response and error helpers |

## Rules

- Do not put business workflows in shared libraries.
- Do not put service-specific entities in shared libraries.
- Keep APIs small and backward-compatible.
- Prefer copying tiny domain concepts into the owning service over creating premature shared abstractions.
