package com.stackwork360.auditservice.domain;

import java.time.Instant;
import java.util.Objects;

public final class AuditRetentionPolicy {
    private final String tenantId;
    private int standardRetentionDays;
    private int restrictedRetentionDays;
    private boolean legalHoldEnabled;
    private Instant updatedAt;

    private AuditRetentionPolicy(
            String tenantId,
            int standardRetentionDays,
            int restrictedRetentionDays,
            boolean legalHoldEnabled,
            Instant updatedAt
    ) {
        this.tenantId = requireText(tenantId, "tenant id is required");
        validateDays(standardRetentionDays);
        validateDays(restrictedRetentionDays);
        this.standardRetentionDays = standardRetentionDays;
        this.restrictedRetentionDays = restrictedRetentionDays;
        this.legalHoldEnabled = legalHoldEnabled;
        this.updatedAt = Objects.requireNonNull(updatedAt, "updated at is required");
    }

    public static AuditRetentionPolicy defaults(String tenantId) {
        return new AuditRetentionPolicy(tenantId, 365, 2555, false, Instant.now());
    }

    public void update(int standardRetentionDays, int restrictedRetentionDays, boolean legalHoldEnabled) {
        validateDays(standardRetentionDays);
        validateDays(restrictedRetentionDays);
        if (restrictedRetentionDays < standardRetentionDays) {
            throw new IllegalArgumentException("restricted retention must be at least standard retention");
        }
        this.standardRetentionDays = standardRetentionDays;
        this.restrictedRetentionDays = restrictedRetentionDays;
        this.legalHoldEnabled = legalHoldEnabled;
        this.updatedAt = Instant.now();
    }

    public int retentionDaysFor(AuditSensitivity sensitivity) {
        if (legalHoldEnabled) {
            return Integer.MAX_VALUE;
        }
        return sensitivity == AuditSensitivity.RESTRICTED ? restrictedRetentionDays : standardRetentionDays;
    }

    public String tenantId() {
        return tenantId;
    }

    public int standardRetentionDays() {
        return standardRetentionDays;
    }

    public int restrictedRetentionDays() {
        return restrictedRetentionDays;
    }

    public boolean legalHoldEnabled() {
        return legalHoldEnabled;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    private static void validateDays(int days) {
        if (days < 30) {
            throw new IllegalArgumentException("retention must be at least 30 days");
        }
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
