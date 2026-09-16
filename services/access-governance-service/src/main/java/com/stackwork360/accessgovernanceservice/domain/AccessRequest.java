package com.stackwork360.accessgovernanceservice.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class AccessRequest {
    private final UUID id;
    private final String tenantId;
    private final String requesterId;
    private final String resourceCode;
    private final AccessLevel level;
    private final String justification;
    private AccessRequestStatus status;
    private String decisionReason;
    private final Instant requestedAt;
    private Instant updatedAt;

    public AccessRequest(UUID id, String tenantId, String requesterId, String resourceCode, AccessLevel level, String justification, AccessRequestStatus status, String decisionReason, Instant requestedAt, Instant updatedAt) {
        this.id = id == null ? UUID.randomUUID() : id;
        this.tenantId = requireText(tenantId, "tenant id is required");
        this.requesterId = requireText(requesterId, "requester id is required");
        this.resourceCode = requireText(resourceCode, "resource code is required");
        this.level = Objects.requireNonNull(level, "access level is required");
        this.justification = requireText(justification, "justification is required");
        this.status = Objects.requireNonNull(status, "request status is required");
        this.decisionReason = decisionReason;
        this.requestedAt = requestedAt == null ? Instant.now() : requestedAt;
        this.updatedAt = updatedAt == null ? this.requestedAt : updatedAt;
    }

    public void approve(String reason) {
        decide(reason, AccessRequestStatus.APPROVED);
    }

    public void reject(String reason) {
        decide(reason, AccessRequestStatus.REJECTED);
    }

    public void provision() {
        if (status != AccessRequestStatus.APPROVED) {
            throw new IllegalStateException("only approved access requests can be provisioned");
        }
        status = AccessRequestStatus.PROVISIONED;
        updatedAt = Instant.now();
    }

    public void requestRemoval(String reason) {
        if (status != AccessRequestStatus.PROVISIONED) {
            throw new IllegalStateException("only provisioned access can be removed");
        }
        decisionReason = requireText(reason, "removal reason is required");
        status = AccessRequestStatus.REMOVAL_REQUESTED;
        updatedAt = Instant.now();
    }

    public void markRemoved() {
        if (status != AccessRequestStatus.REMOVAL_REQUESTED) {
            throw new IllegalStateException("only removal-requested access can be marked removed");
        }
        status = AccessRequestStatus.REMOVED;
        updatedAt = Instant.now();
    }

    public UUID id() { return id; }
    public String tenantId() { return tenantId; }
    public String requesterId() { return requesterId; }
    public String resourceCode() { return resourceCode; }
    public AccessLevel level() { return level; }
    public String justification() { return justification; }
    public AccessRequestStatus status() { return status; }
    public String decisionReason() { return decisionReason; }
    public Instant requestedAt() { return requestedAt; }
    public Instant updatedAt() { return updatedAt; }

    private void decide(String reason, AccessRequestStatus status) {
        if (this.status != AccessRequestStatus.REQUESTED) {
            throw new IllegalStateException("access request has already been decided");
        }
        decisionReason = requireText(reason, "decision reason is required");
        this.status = status;
        updatedAt = Instant.now();
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
