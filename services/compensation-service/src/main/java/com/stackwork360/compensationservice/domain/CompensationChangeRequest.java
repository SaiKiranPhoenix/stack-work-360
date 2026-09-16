package com.stackwork360.compensationservice.domain;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public final class CompensationChangeRequest {
    private final UUID id;
    private final String tenantId;
    private final String workerId;
    private final String currentJobLevel;
    private final String proposedJobLevel;
    private final Money currentSalary;
    private final Money proposedSalary;
    private final LocalDate effectiveDate;
    private final CompensationChangeReason reason;
    private CompensationChangeStatus status;
    private String decidedBy;
    private final Instant createdAt;
    private Instant updatedAt;

    private CompensationChangeRequest(UUID id, String tenantId, String workerId, String currentJobLevel, String proposedJobLevel, Money currentSalary, Money proposedSalary, LocalDate effectiveDate, CompensationChangeReason reason, CompensationChangeStatus status, String decidedBy, Instant createdAt, Instant updatedAt) {
        this.id = Objects.requireNonNull(id, "request id is required");
        this.tenantId = requireText(tenantId, "tenant id is required");
        this.workerId = requireText(workerId, "worker id is required");
        this.currentJobLevel = requireText(currentJobLevel, "current job level is required");
        this.proposedJobLevel = requireText(proposedJobLevel, "proposed job level is required");
        this.currentSalary = Objects.requireNonNull(currentSalary, "current salary is required");
        this.proposedSalary = Objects.requireNonNull(proposedSalary, "proposed salary is required");
        this.effectiveDate = Objects.requireNonNull(effectiveDate, "effective date is required");
        this.reason = Objects.requireNonNull(reason, "reason is required");
        this.status = Objects.requireNonNull(status, "status is required");
        this.decidedBy = decidedBy;
        this.createdAt = Objects.requireNonNull(createdAt, "created at is required");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updated at is required");
    }

    public static CompensationChangeRequest request(String tenantId, String workerId, String currentJobLevel, String proposedJobLevel, Money currentSalary, Money proposedSalary, LocalDate effectiveDate, CompensationChangeReason reason) {
        Instant now = Instant.now();
        return new CompensationChangeRequest(UUID.randomUUID(), tenantId, workerId, currentJobLevel, proposedJobLevel, currentSalary, proposedSalary, effectiveDate, reason, CompensationChangeStatus.REQUESTED, null, now, now);
    }

    public void approve(String actorId) {
        decide(actorId, CompensationChangeStatus.APPROVED);
    }

    public void reject(String actorId) {
        decide(actorId, CompensationChangeStatus.REJECTED);
    }

    public void apply() {
        if (status != CompensationChangeStatus.APPROVED) {
            throw new IllegalStateException("only approved compensation changes can be applied");
        }
        status = CompensationChangeStatus.APPLIED;
        updatedAt = Instant.now();
    }

    public Money budgetImpact() {
        return proposedSalary.minus(currentSalary);
    }

    public boolean promotion() {
        return reason == CompensationChangeReason.PROMOTION && !currentJobLevel.equals(proposedJobLevel);
    }

    public UUID id() { return id; }
    public String tenantId() { return tenantId; }
    public String workerId() { return workerId; }
    public String currentJobLevel() { return currentJobLevel; }
    public String proposedJobLevel() { return proposedJobLevel; }
    public Money currentSalary() { return currentSalary; }
    public Money proposedSalary() { return proposedSalary; }
    public LocalDate effectiveDate() { return effectiveDate; }
    public CompensationChangeReason reason() { return reason; }
    public CompensationChangeStatus status() { return status; }
    public String decidedBy() { return decidedBy; }
    public Instant createdAt() { return createdAt; }
    public Instant updatedAt() { return updatedAt; }

    private void decide(String actorId, CompensationChangeStatus status) {
        if (this.status != CompensationChangeStatus.REQUESTED) {
            throw new IllegalStateException("compensation change has already been decided");
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
