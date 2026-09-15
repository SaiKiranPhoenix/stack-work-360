package com.stackwork360.organizationservice.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class OrgUnit {
    private final UUID id;
    private final String tenantId;
    private String name;
    private OrgUnitType type;
    private UUID parentId;
    private String leaderWorkerId;
    private String locationCode;
    private String costCenterCode;
    private boolean active;
    private final Instant createdAt;
    private Instant updatedAt;

    private OrgUnit(
            UUID id,
            String tenantId,
            String name,
            OrgUnitType type,
            UUID parentId,
            String leaderWorkerId,
            String locationCode,
            String costCenterCode,
            boolean active,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = Objects.requireNonNull(id, "org unit id is required");
        this.tenantId = requireText(tenantId, "tenant id is required");
        this.name = requireText(name, "org unit name is required");
        this.type = Objects.requireNonNull(type, "org unit type is required");
        this.parentId = parentId;
        this.leaderWorkerId = blankToNull(leaderWorkerId);
        this.locationCode = normalizeOptionalCode(locationCode);
        this.costCenterCode = normalizeOptionalCode(costCenterCode);
        this.active = active;
        this.createdAt = Objects.requireNonNull(createdAt, "created at is required");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updated at is required");
    }

    public static OrgUnit create(
            String tenantId,
            String name,
            OrgUnitType type,
            UUID parentId,
            String leaderWorkerId,
            String locationCode,
            String costCenterCode
    ) {
        Instant now = Instant.now();
        return new OrgUnit(
                UUID.randomUUID(),
                tenantId,
                name,
                type,
                parentId,
                leaderWorkerId,
                locationCode,
                costCenterCode,
                true,
                now,
                now
        );
    }

    public void update(
            String name,
            UUID parentId,
            String leaderWorkerId,
            String locationCode,
            String costCenterCode
    ) {
        this.name = requireText(name, "org unit name is required");
        this.parentId = parentId;
        this.leaderWorkerId = blankToNull(leaderWorkerId);
        this.locationCode = normalizeOptionalCode(locationCode);
        this.costCenterCode = normalizeOptionalCode(costCenterCode);
        this.updatedAt = Instant.now();
    }

    public void deactivate() {
        this.active = false;
        this.updatedAt = Instant.now();
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

    public OrgUnitType type() {
        return type;
    }

    public UUID parentId() {
        return parentId;
    }

    public String leaderWorkerId() {
        return leaderWorkerId;
    }

    public String locationCode() {
        return locationCode;
    }

    public String costCenterCode() {
        return costCenterCode;
    }

    public boolean active() {
        return active;
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

    private static String normalizeOptionalCode(String value) {
        return value == null || value.isBlank() ? null : value.trim().toUpperCase();
    }

    private static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
