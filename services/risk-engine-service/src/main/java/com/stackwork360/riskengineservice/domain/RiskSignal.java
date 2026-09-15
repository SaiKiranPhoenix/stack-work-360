package com.stackwork360.riskengineservice.domain;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class RiskSignal {
    private final UUID id;
    private final String tenantId;
    private final RiskSource source;
    private final String subjectId;
    private final UUID ruleId;
    private final String ruleName;
    private final RiskSeverity severity;
    private final String explanation;
    private final Map<String, String> facts;
    private RiskSignalStatus status;
    private String resolvedBy;
    private Instant resolvedAt;
    private final Instant createdAt;
    private Instant updatedAt;

    private RiskSignal(
            UUID id,
            String tenantId,
            RiskSource source,
            String subjectId,
            UUID ruleId,
            String ruleName,
            RiskSeverity severity,
            String explanation,
            Map<String, String> facts,
            RiskSignalStatus status,
            String resolvedBy,
            Instant resolvedAt,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = Objects.requireNonNull(id, "risk signal id is required");
        this.tenantId = requireText(tenantId, "tenant id is required");
        this.source = Objects.requireNonNull(source, "source is required");
        this.subjectId = requireText(subjectId, "subject id is required");
        this.ruleId = Objects.requireNonNull(ruleId, "rule id is required");
        this.ruleName = requireText(ruleName, "rule name is required");
        this.severity = Objects.requireNonNull(severity, "severity is required");
        this.explanation = requireText(explanation, "explanation is required");
        this.facts = Map.copyOf(facts == null ? Map.of() : facts);
        this.status = Objects.requireNonNull(status, "risk signal status is required");
        this.resolvedBy = resolvedBy;
        this.resolvedAt = resolvedAt;
        this.createdAt = Objects.requireNonNull(createdAt, "created at is required");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updated at is required");
    }

    public static RiskSignal open(
            String tenantId,
            RiskSource source,
            String subjectId,
            UUID ruleId,
            String ruleName,
            RiskSeverity severity,
            String explanation,
            Map<String, String> facts
    ) {
        Instant now = Instant.now();
        return new RiskSignal(UUID.randomUUID(), tenantId, source, subjectId, ruleId, ruleName, severity, explanation, facts, RiskSignalStatus.OPEN, null, null, now, now);
    }

    public void acknowledge() {
        if (status != RiskSignalStatus.OPEN) {
            throw new IllegalStateException("only open signals can be acknowledged");
        }
        status = RiskSignalStatus.ACKNOWLEDGED;
        updatedAt = Instant.now();
    }

    public void resolve(String actorId) {
        if (status == RiskSignalStatus.RESOLVED || status == RiskSignalStatus.DISMISSED) {
            throw new IllegalStateException("risk signal is already closed");
        }
        status = RiskSignalStatus.RESOLVED;
        resolvedBy = requireText(actorId, "actor id is required");
        resolvedAt = Instant.now();
        updatedAt = resolvedAt;
    }

    public void dismiss(String actorId) {
        if (status == RiskSignalStatus.RESOLVED || status == RiskSignalStatus.DISMISSED) {
            throw new IllegalStateException("risk signal is already closed");
        }
        status = RiskSignalStatus.DISMISSED;
        resolvedBy = requireText(actorId, "actor id is required");
        resolvedAt = Instant.now();
        updatedAt = resolvedAt;
    }

    public UUID id() {
        return id;
    }

    public String tenantId() {
        return tenantId;
    }

    public RiskSource source() {
        return source;
    }

    public String subjectId() {
        return subjectId;
    }

    public UUID ruleId() {
        return ruleId;
    }

    public String ruleName() {
        return ruleName;
    }

    public RiskSeverity severity() {
        return severity;
    }

    public String explanation() {
        return explanation;
    }

    public Map<String, String> facts() {
        return facts;
    }

    public RiskSignalStatus status() {
        return status;
    }

    public String resolvedBy() {
        return resolvedBy;
    }

    public Instant resolvedAt() {
        return resolvedAt;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
