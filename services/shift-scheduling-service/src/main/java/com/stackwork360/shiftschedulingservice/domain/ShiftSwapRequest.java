package com.stackwork360.shiftschedulingservice.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class ShiftSwapRequest {
    private final UUID id;
    private final String tenantId;
    private final UUID assignmentId;
    private final String requestedBy;
    private final String targetWorkerId;
    private final String reason;
    private ShiftSwapStatus status;
    private String decidedBy;
    private final Instant createdAt;
    private Instant updatedAt;

    private ShiftSwapRequest(UUID id, String tenantId, UUID assignmentId, String requestedBy, String targetWorkerId, String reason, ShiftSwapStatus status, String decidedBy, Instant createdAt, Instant updatedAt) {
        this.id = Objects.requireNonNull(id, "swap id is required");
        this.tenantId = requireText(tenantId, "tenant id is required");
        this.assignmentId = Objects.requireNonNull(assignmentId, "assignment id is required");
        this.requestedBy = requireText(requestedBy, "requested by is required");
        this.targetWorkerId = requireText(targetWorkerId, "target worker id is required");
        this.reason = requireText(reason, "reason is required");
        this.status = Objects.requireNonNull(status, "swap status is required");
        this.decidedBy = decidedBy;
        this.createdAt = Objects.requireNonNull(createdAt, "created at is required");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updated at is required");
    }

    public static ShiftSwapRequest request(String tenantId, UUID assignmentId, String requestedBy, String targetWorkerId, String reason) {
        Instant now = Instant.now();
        return new ShiftSwapRequest(UUID.randomUUID(), tenantId, assignmentId, requestedBy, targetWorkerId, reason, ShiftSwapStatus.REQUESTED, null, now, now);
    }

    public void approve(String actorId) {
        decide(actorId, ShiftSwapStatus.APPROVED);
    }

    public void reject(String actorId) {
        decide(actorId, ShiftSwapStatus.REJECTED);
    }

    public UUID id() {
        return id;
    }

    public String tenantId() {
        return tenantId;
    }

    public UUID assignmentId() {
        return assignmentId;
    }

    public String requestedBy() {
        return requestedBy;
    }

    public String targetWorkerId() {
        return targetWorkerId;
    }

    public String reason() {
        return reason;
    }

    public ShiftSwapStatus status() {
        return status;
    }

    public String decidedBy() {
        return decidedBy;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    private void decide(String actorId, ShiftSwapStatus status) {
        if (this.status != ShiftSwapStatus.REQUESTED) {
            throw new IllegalStateException("swap request has already been decided");
        }
        this.decidedBy = requireText(actorId, "actor id is required");
        this.status = status;
        this.updatedAt = Instant.now();
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
