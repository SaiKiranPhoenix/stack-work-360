package com.stackwork360.assetmanagementservice.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class Asset {
    private final UUID id;
    private final String tenantId;
    private final String assetTag;
    private final AssetType type;
    private final String model;
    private final String serialNumber;
    private AssetStatus status;
    private String assignedTo;
    private Instant updatedAt;

    public Asset(UUID id, String tenantId, String assetTag, AssetType type, String model, String serialNumber, AssetStatus status, String assignedTo, Instant updatedAt) {
        this.id = id == null ? UUID.randomUUID() : id;
        this.tenantId = requireText(tenantId, "tenant id is required");
        this.assetTag = requireText(assetTag, "asset tag is required");
        this.type = Objects.requireNonNull(type, "asset type is required");
        this.model = requireText(model, "model is required");
        this.serialNumber = requireText(serialNumber, "serial number is required");
        this.status = Objects.requireNonNull(status, "status is required");
        this.assignedTo = assignedTo;
        this.updatedAt = updatedAt == null ? Instant.now() : updatedAt;
    }

    public void assignTo(String workerId) {
        if (status != AssetStatus.AVAILABLE && status != AssetStatus.RETURNED) {
            throw new IllegalStateException("only available or returned assets can be assigned");
        }
        assignedTo = requireText(workerId, "worker id is required");
        status = AssetStatus.ASSIGNED;
        updatedAt = Instant.now();
    }

    public void returnAsset() {
        if (status != AssetStatus.ASSIGNED) {
            throw new IllegalStateException("only assigned assets can be returned");
        }
        assignedTo = null;
        status = AssetStatus.RETURNED;
        updatedAt = Instant.now();
    }

    public void sendForRepair() {
        if (status == AssetStatus.ASSIGNED) {
            throw new IllegalStateException("assigned assets must be returned before repair");
        }
        status = AssetStatus.IN_REPAIR;
        updatedAt = Instant.now();
    }

    public void markRepaired() {
        if (status != AssetStatus.IN_REPAIR) {
            throw new IllegalStateException("only assets in repair can be repaired");
        }
        status = AssetStatus.AVAILABLE;
        updatedAt = Instant.now();
    }

    public UUID id() { return id; }
    public String tenantId() { return tenantId; }
    public String assetTag() { return assetTag; }
    public AssetType type() { return type; }
    public String model() { return model; }
    public String serialNumber() { return serialNumber; }
    public AssetStatus status() { return status; }
    public String assignedTo() { return assignedTo; }
    public Instant updatedAt() { return updatedAt; }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
