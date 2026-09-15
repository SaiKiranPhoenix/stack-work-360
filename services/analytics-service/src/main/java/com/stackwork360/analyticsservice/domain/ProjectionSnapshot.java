package com.stackwork360.analyticsservice.domain;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class ProjectionSnapshot {
    private final UUID id;
    private final String tenantId;
    private final ProjectionType type;
    private final String scope;
    private final List<AnalyticsMetric> metrics;
    private final Instant generatedAt;

    private ProjectionSnapshot(UUID id, String tenantId, ProjectionType type, String scope, List<AnalyticsMetric> metrics, Instant generatedAt) {
        this.id = Objects.requireNonNull(id, "projection id is required");
        this.tenantId = requireText(tenantId, "tenant id is required");
        this.type = Objects.requireNonNull(type, "projection type is required");
        this.scope = requireText(scope, "projection scope is required");
        this.metrics = List.copyOf(metrics == null ? List.of() : metrics);
        if (this.metrics.isEmpty()) {
            throw new IllegalArgumentException("projection must contain at least one metric");
        }
        this.generatedAt = Objects.requireNonNull(generatedAt, "generated at is required");
    }

    public static ProjectionSnapshot create(String tenantId, ProjectionType type, String scope, List<AnalyticsMetric> metrics) {
        return new ProjectionSnapshot(UUID.randomUUID(), tenantId, type, scope, metrics, Instant.now());
    }

    public AnalyticsMetric metric(String key) {
        return metrics.stream()
                .filter(metric -> metric.key().equals(key))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("metric not found: " + key));
    }

    public UUID id() {
        return id;
    }

    public String tenantId() {
        return tenantId;
    }

    public ProjectionType type() {
        return type;
    }

    public String scope() {
        return scope;
    }

    public List<AnalyticsMetric> metrics() {
        return metrics;
    }

    public Instant generatedAt() {
        return generatedAt;
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
