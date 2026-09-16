package com.stackwork360.talentmarketplaceservice.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class InternalApplication {
    private final UUID id;
    private final String tenantId;
    private final UUID opportunityId;
    private final String workerId;
    private final String managerId;
    private final String note;
    private ApplicationStatus status;
    private String decisionReason;
    private final Instant appliedAt;
    private Instant updatedAt;

    public InternalApplication(UUID id, String tenantId, UUID opportunityId, String workerId, String managerId, String note, ApplicationStatus status, String decisionReason, Instant appliedAt, Instant updatedAt) {
        this.id = id == null ? UUID.randomUUID() : id;
        this.tenantId = requireText(tenantId, "tenant id is required");
        this.opportunityId = Objects.requireNonNull(opportunityId, "opportunity id is required");
        this.workerId = requireText(workerId, "worker id is required");
        this.managerId = requireText(managerId, "manager id is required");
        this.note = requireText(note, "note is required");
        this.status = Objects.requireNonNull(status, "status is required");
        this.decisionReason = decisionReason;
        this.appliedAt = appliedAt == null ? Instant.now() : appliedAt;
        this.updatedAt = updatedAt == null ? this.appliedAt : updatedAt;
    }

    public void managerApprove(String reason) {
        decide(reason, ApplicationStatus.MANAGER_APPROVED);
    }

    public void managerReject(String reason) {
        decide(reason, ApplicationStatus.MANAGER_REJECTED);
    }

    public void accept(String reason) {
        if (status != ApplicationStatus.MANAGER_APPROVED) {
            throw new IllegalStateException("application requires manager approval before acceptance");
        }
        decide(reason, ApplicationStatus.ACCEPTED);
    }

    public UUID id() { return id; }
    public String tenantId() { return tenantId; }
    public UUID opportunityId() { return opportunityId; }
    public String workerId() { return workerId; }
    public String managerId() { return managerId; }
    public String note() { return note; }
    public ApplicationStatus status() { return status; }
    public String decisionReason() { return decisionReason; }
    public Instant appliedAt() { return appliedAt; }
    public Instant updatedAt() { return updatedAt; }

    private void decide(String reason, ApplicationStatus status) {
        if (this.status != ApplicationStatus.SUBMITTED && this.status != ApplicationStatus.MANAGER_APPROVED) {
            throw new IllegalStateException("application has already been decided");
        }
        this.decisionReason = requireText(reason, "decision reason is required");
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
