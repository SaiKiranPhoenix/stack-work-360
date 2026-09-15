package com.stackwork360.riskengineservice.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class RiskRule {
    private final UUID id;
    private final String tenantId;
    private String name;
    private String description;
    private RiskSource source;
    private RiskSeverity severity;
    private boolean enabled;
    private final List<RiskCondition> conditions;
    private final Instant createdAt;
    private Instant updatedAt;

    private RiskRule(
            UUID id,
            String tenantId,
            String name,
            String description,
            RiskSource source,
            RiskSeverity severity,
            boolean enabled,
            List<RiskCondition> conditions,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = Objects.requireNonNull(id, "risk rule id is required");
        this.tenantId = requireText(tenantId, "tenant id is required");
        this.name = requireText(name, "rule name is required");
        this.description = requireText(description, "rule description is required");
        this.source = Objects.requireNonNull(source, "source is required");
        this.severity = Objects.requireNonNull(severity, "severity is required");
        this.enabled = enabled;
        this.conditions = new ArrayList<>();
        (conditions == null ? List.<RiskCondition>of() : conditions).forEach(this::addCondition);
        if (this.conditions.isEmpty()) {
            throw new IllegalArgumentException("risk rule must have at least one condition");
        }
        this.createdAt = Objects.requireNonNull(createdAt, "created at is required");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updated at is required");
    }

    public static RiskRule create(
            String tenantId,
            String name,
            String description,
            RiskSource source,
            RiskSeverity severity,
            List<RiskCondition> conditions
    ) {
        Instant now = Instant.now();
        return new RiskRule(UUID.randomUUID(), tenantId, name, description, source, severity, true, conditions, now, now);
    }

    public void update(String name, String description, RiskSource source, RiskSeverity severity, boolean enabled, List<RiskCondition> conditions) {
        this.name = requireText(name, "rule name is required");
        this.description = requireText(description, "rule description is required");
        this.source = Objects.requireNonNull(source, "source is required");
        this.severity = Objects.requireNonNull(severity, "severity is required");
        this.enabled = enabled;
        this.conditions.clear();
        (conditions == null ? List.<RiskCondition>of() : conditions).forEach(this::addCondition);
        if (this.conditions.isEmpty()) {
            throw new IllegalArgumentException("risk rule must have at least one condition");
        }
        updatedAt = Instant.now();
    }

    public boolean matches(RiskFacts facts) {
        return enabled
                && source == facts.source()
                && conditions.stream().allMatch(condition -> condition.matches(facts.values()));
    }

    public RiskSignal toSignal(RiskFacts facts) {
        if (!matches(facts)) {
            throw new IllegalStateException("risk facts do not match rule");
        }
        return RiskSignal.open(
                tenantId,
                facts.source(),
                facts.subjectId(),
                id,
                name,
                severity,
                explanationFor(facts),
                facts.values()
        );
    }

    public String explanationFor(RiskFacts facts) {
        return "Rule '" + name + "' matched " + conditions.stream()
                .map(RiskCondition::explain)
                .toList()
                + " for subject " + facts.subjectId();
    }

    public UUID id() {
        return id;
    }

    public String tenantId() {
        return tenantId;
    }

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public RiskSource source() {
        return source;
    }

    public RiskSeverity severity() {
        return severity;
    }

    public boolean enabled() {
        return enabled;
    }

    public List<RiskCondition> conditions() {
        return List.copyOf(conditions);
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    private void addCondition(RiskCondition condition) {
        conditions.add(Objects.requireNonNull(condition, "risk condition is required"));
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
