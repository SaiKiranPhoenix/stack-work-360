package com.stackwork360.tenantservice.domain;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public final class Tenant {
    private final UUID id;
    private final String slug;
    private String displayName;
    private TenantStatus status;
    private TenantPlan plan;
    private DataResidencyRegion dataResidencyRegion;
    private int retentionDays;
    private Set<String> enabledFeatures;
    private Map<String, String> configuration;
    private final Instant createdAt;
    private Instant updatedAt;

    private Tenant(
            UUID id,
            String slug,
            String displayName,
            TenantStatus status,
            TenantPlan plan,
            DataResidencyRegion dataResidencyRegion,
            int retentionDays,
            Set<String> enabledFeatures,
            Map<String, String> configuration,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = Objects.requireNonNull(id, "tenant id is required");
        this.slug = requireText(slug, "tenant slug is required");
        this.displayName = requireText(displayName, "tenant display name is required");
        this.status = Objects.requireNonNull(status, "tenant status is required");
        this.plan = Objects.requireNonNull(plan, "tenant plan is required");
        this.dataResidencyRegion = Objects.requireNonNull(dataResidencyRegion, "data residency region is required");
        this.retentionDays = validateRetention(retentionDays);
        this.enabledFeatures = Set.copyOf(enabledFeatures);
        this.configuration = Map.copyOf(configuration);
        this.createdAt = Objects.requireNonNull(createdAt, "created at is required");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updated at is required");
    }

    public static Tenant restore(
            UUID id,
            String slug,
            String displayName,
            TenantStatus status,
            TenantPlan plan,
            DataResidencyRegion dataResidencyRegion,
            int retentionDays,
            Set<String> enabledFeatures,
            Map<String, String> configuration,
            Instant createdAt,
            Instant updatedAt
    ) {
        return new Tenant(
                id,
                slug,
                displayName,
                status,
                plan,
                dataResidencyRegion,
                retentionDays,
                nullToEmpty(enabledFeatures),
                nullToEmpty(configuration),
                createdAt,
                updatedAt
        );
    }

    public static Tenant provision(
            String slug,
            String displayName,
            TenantPlan plan,
            DataResidencyRegion dataResidencyRegion,
            int retentionDays,
            Set<String> enabledFeatures,
            Map<String, String> configuration
    ) {
        Instant now = Instant.now();
        return new Tenant(
                UUID.randomUUID(),
                normalizeSlug(slug),
                displayName,
                TenantStatus.ACTIVE,
                plan,
                dataResidencyRegion,
                retentionDays,
                nullToEmpty(enabledFeatures),
                nullToEmpty(configuration),
                now,
                now
        );
    }

    public void updateSettings(
            String displayName,
            TenantPlan plan,
            DataResidencyRegion dataResidencyRegion,
            int retentionDays,
            Set<String> enabledFeatures,
            Map<String, String> configuration
    ) {
        ensureMutable();
        this.displayName = requireText(displayName, "tenant display name is required");
        this.plan = Objects.requireNonNull(plan, "tenant plan is required");
        this.dataResidencyRegion = Objects.requireNonNull(dataResidencyRegion, "data residency region is required");
        this.retentionDays = validateRetention(retentionDays);
        this.enabledFeatures = Set.copyOf(nullToEmpty(enabledFeatures));
        this.configuration = Map.copyOf(nullToEmpty(configuration));
        this.updatedAt = Instant.now();
    }

    public void assignFeatures(Set<String> enabledFeatures) {
        ensureMutable();
        this.enabledFeatures = Set.copyOf(nullToEmpty(enabledFeatures));
        this.updatedAt = Instant.now();
    }

    public void suspend() {
        ensureMutable();
        this.status = TenantStatus.SUSPENDED;
        this.updatedAt = Instant.now();
    }

    public void reactivate() {
        if (status == TenantStatus.DELETED) {
            throw new IllegalStateException("deleted tenant cannot be reactivated");
        }
        this.status = TenantStatus.ACTIVE;
        this.updatedAt = Instant.now();
    }

    public void markDeleting() {
        ensureMutable();
        this.status = TenantStatus.DEACTIVATING;
        this.updatedAt = Instant.now();
    }

    public UUID id() {
        return id;
    }

    public String slug() {
        return slug;
    }

    public String displayName() {
        return displayName;
    }

    public TenantStatus status() {
        return status;
    }

    public TenantPlan plan() {
        return plan;
    }

    public DataResidencyRegion dataResidencyRegion() {
        return dataResidencyRegion;
    }

    public int retentionDays() {
        return retentionDays;
    }

    public Set<String> enabledFeatures() {
        return Collections.unmodifiableSet(enabledFeatures);
    }

    public Map<String, String> configuration() {
        return Collections.unmodifiableMap(configuration);
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    private void ensureMutable() {
        if (status == TenantStatus.DELETED || status == TenantStatus.DEACTIVATING) {
            throw new IllegalStateException("tenant is not mutable in status " + status);
        }
    }

    private static int validateRetention(int retentionDays) {
        if (retentionDays < 30 || retentionDays > 3650) {
            throw new IllegalArgumentException("retention days must be between 30 and 3650");
        }
        return retentionDays;
    }

    private static String normalizeSlug(String slug) {
        String normalized = requireText(slug, "tenant slug is required").toLowerCase();
        if (!normalized.matches("^[a-z0-9][a-z0-9-]{2,62}$")) {
            throw new IllegalArgumentException("tenant slug must be 3-63 lowercase letters, numbers, or hyphens");
        }
        return normalized;
    }

    private static String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }

    private static <T> Set<T> nullToEmpty(Set<T> values) {
        return values == null ? Set.of() : values;
    }

    private static <K, V> Map<K, V> nullToEmpty(Map<K, V> values) {
        return values == null ? Map.of() : values;
    }
}
