package com.stackwork360.benefitsservice.domain;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public final class BenefitChangeRequest {
    private final UUID id;
    private final String tenantId;
    private final String workerId;
    private final String planCode;
    private final BenefitChangeType type;
    private final CoverageTier requestedTier;
    private final LocalDate effectiveDate;
    private BenefitChangeStatus status;
    private String decidedBy;
    private final Instant createdAt;
    private Instant updatedAt;

    private BenefitChangeRequest(UUID id, String tenantId, String workerId, String planCode, BenefitChangeType type, CoverageTier requestedTier, LocalDate effectiveDate, BenefitChangeStatus status, String decidedBy, Instant createdAt, Instant updatedAt) {
        this.id = Objects.requireNonNull(id, "change id is required");
        this.tenantId = requireText(tenantId, "tenant id is required");
        this.workerId = requireText(workerId, "worker id is required");
        this.planCode = requireText(planCode, "plan code is required");
        this.type = Objects.requireNonNull(type, "change type is required");
        this.requestedTier = Objects.requireNonNull(requestedTier, "requested tier is required");
        this.effectiveDate = Objects.requireNonNull(effectiveDate, "effective date is required");
        this.status = Objects.requireNonNull(status, "status is required");
        this.decidedBy = decidedBy;
        this.createdAt = Objects.requireNonNull(createdAt, "created at is required");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updated at is required");
    }

    public static BenefitChangeRequest request(String tenantId, String workerId, String planCode, BenefitChangeType type, CoverageTier requestedTier, LocalDate effectiveDate) {
        Instant now = Instant.now();
        return new BenefitChangeRequest(UUID.randomUUID(), tenantId, workerId, planCode, type, requestedTier, effectiveDate, BenefitChangeStatus.REQUESTED, null, now, now);
    }

    public void approve(String actorId) {
        decide(actorId, BenefitChangeStatus.APPROVED);
    }

    public void reject(String actorId) {
        decide(actorId, BenefitChangeStatus.REJECTED);
    }

    public void apply() {
        if (status != BenefitChangeStatus.APPROVED) {
            throw new IllegalStateException("only approved benefit changes can be applied");
        }
        status = BenefitChangeStatus.APPLIED;
        updatedAt = Instant.now();
    }

    public UUID id() { return id; }
    public String tenantId() { return tenantId; }
    public String workerId() { return workerId; }
    public String planCode() { return planCode; }
    public BenefitChangeType type() { return type; }
    public CoverageTier requestedTier() { return requestedTier; }
    public LocalDate effectiveDate() { return effectiveDate; }
    public BenefitChangeStatus status() { return status; }
    public String decidedBy() { return decidedBy; }
    public Instant createdAt() { return createdAt; }
    public Instant updatedAt() { return updatedAt; }

    private void decide(String actorId, BenefitChangeStatus status) {
        if (this.status != BenefitChangeStatus.REQUESTED) {
            throw new IllegalStateException("benefit change has already been decided");
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
