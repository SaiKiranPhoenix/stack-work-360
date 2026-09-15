package com.stackwork360.auditservice.domain;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class AuditRecord {
    private final UUID id;
    private final String tenantId;
    private final String actorId;
    private final String serviceName;
    private final String resourceType;
    private final String resourceId;
    private final AuditAction action;
    private final AuditOutcome outcome;
    private final AuditSensitivity sensitivity;
    private final String reason;
    private final String correlationId;
    private final Map<String, String> metadata;
    private final Instant occurredAt;

    private AuditRecord(
            UUID id,
            String tenantId,
            String actorId,
            String serviceName,
            String resourceType,
            String resourceId,
            AuditAction action,
            AuditOutcome outcome,
            AuditSensitivity sensitivity,
            String reason,
            String correlationId,
            Map<String, String> metadata,
            Instant occurredAt
    ) {
        this.id = Objects.requireNonNull(id, "audit record id is required");
        this.tenantId = requireText(tenantId, "tenant id is required");
        this.actorId = requireText(actorId, "actor id is required");
        this.serviceName = requireText(serviceName, "service name is required");
        this.resourceType = requireText(resourceType, "resource type is required");
        this.resourceId = requireText(resourceId, "resource id is required");
        this.action = Objects.requireNonNull(action, "audit action is required");
        this.outcome = Objects.requireNonNull(outcome, "audit outcome is required");
        this.sensitivity = Objects.requireNonNull(sensitivity, "audit sensitivity is required");
        this.reason = requireText(reason, "audit reason is required");
        this.correlationId = requireText(correlationId, "correlation id is required");
        this.metadata = Map.copyOf(metadata == null ? Map.of() : metadata);
        this.occurredAt = Objects.requireNonNull(occurredAt, "occurred at is required");
    }

    public static AuditRecord record(
            String tenantId,
            String actorId,
            String serviceName,
            String resourceType,
            String resourceId,
            AuditAction action,
            AuditOutcome outcome,
            AuditSensitivity sensitivity,
            String reason,
            String correlationId,
            Map<String, String> metadata
    ) {
        return new AuditRecord(
                UUID.randomUUID(),
                tenantId,
                actorId,
                serviceName,
                resourceType,
                resourceId,
                action,
                outcome,
                sensitivity,
                reason,
                correlationId,
                metadata,
                Instant.now()
        );
    }

    public UUID id() {
        return id;
    }

    public String tenantId() {
        return tenantId;
    }

    public String actorId() {
        return actorId;
    }

    public String serviceName() {
        return serviceName;
    }

    public String resourceType() {
        return resourceType;
    }

    public String resourceId() {
        return resourceId;
    }

    public AuditAction action() {
        return action;
    }

    public AuditOutcome outcome() {
        return outcome;
    }

    public AuditSensitivity sensitivity() {
        return sensitivity;
    }

    public String reason() {
        return reason;
    }

    public String correlationId() {
        return correlationId;
    }

    public Map<String, String> metadata() {
        return metadata;
    }

    public Instant occurredAt() {
        return occurredAt;
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
