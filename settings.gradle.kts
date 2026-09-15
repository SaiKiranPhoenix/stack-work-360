pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
    }
}

rootProject.name = "stack-work-360"

include(
    "libs:common",
    "libs:events",
    "libs:security",
    "libs:web",
)

val services = listOf(
    "tenant-service",
    "auth-service",
    "people-core-service",
    "organization-service",
    "workflow-service",
    "workflow-builder-service",
    "leave-service",
    "attendance-service",
    "shift-scheduling-service",
    "document-service",
    "helpdesk-service",
    "payroll-prep-service",
    "compensation-service",
    "benefits-service",
    "performance-service",
    "goals-okr-service",
    "learning-service",
    "skills-graph-service",
    "talent-marketplace-service",
    "workforce-planning-service",
    "developer-intelligence-service",
    "access-governance-service",
    "asset-management-service",
    "speak-up-case-service",
    "risk-engine-service",
    "policy-assistant-service",
    "manager-copilot-service",
    "notification-service",
    "audit-service",
    "analytics-service",
    "search-service",
    "integration-service",
    "billing-entitlements-service",
    "admin-portal-service",
)

services.forEach { include("services:$it") }
