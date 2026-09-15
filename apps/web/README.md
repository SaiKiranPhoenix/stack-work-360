# Web App

React frontend for stack-work-360.

## Structure

| Path | Purpose |
|---|---|
| `src/app` | Application bootstrap, shell, global styles, route composition |
| `src/features` | Product feature modules |
| `src/shared/api` | API clients and request helpers |
| `src/shared/ui` | Reusable UI components |

## Frontend Rules

- Feature modules should not import from other feature modules directly.
- Cross-feature utilities belong in `shared`.
- Keep API contracts explicit and generated where possible.
- Prefer accessible, predictable enterprise UI over decorative layouts.
- Keep role-based navigation and authorization checks centralized.
