package com.stackwork360.leaveservice.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

public final class LeaveRequest {
    private final UUID id;
    private final String tenantId;
    private final String workerId;
    private final LeaveType leaveType;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final BigDecimal requestedDays;
    private final String reason;
    private LeaveRequestStatus status;
    private String decidedBy;
    private Instant decidedAt;
    private final Instant createdAt;
    private Instant updatedAt;

    private LeaveRequest(
            UUID id,
            String tenantId,
            String workerId,
            LeaveType leaveType,
            LocalDate startDate,
            LocalDate endDate,
            BigDecimal requestedDays,
            String reason,
            LeaveRequestStatus status,
            String decidedBy,
            Instant decidedAt,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = Objects.requireNonNull(id, "leave request id is required");
        this.tenantId = requireText(tenantId, "tenant id is required");
        this.workerId = requireText(workerId, "worker id is required");
        this.leaveType = Objects.requireNonNull(leaveType, "leave type is required");
        this.startDate = Objects.requireNonNull(startDate, "start date is required");
        this.endDate = Objects.requireNonNull(endDate, "end date is required");
        this.requestedDays = positive(requestedDays, "requested days must be positive");
        this.reason = reason == null ? "" : reason.trim();
        this.status = Objects.requireNonNull(status, "leave request status is required");
        this.decidedBy = decidedBy;
        this.decidedAt = decidedAt;
        this.createdAt = Objects.requireNonNull(createdAt, "created at is required");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updated at is required");
        validateDates();
    }

    public static LeaveRequest submit(
            String tenantId,
            String workerId,
            LeaveType leaveType,
            LocalDate startDate,
            LocalDate endDate,
            String reason
    ) {
        Instant now = Instant.now();
        return new LeaveRequest(
                UUID.randomUUID(),
                tenantId,
                workerId,
                leaveType,
                startDate,
                endDate,
                BigDecimal.valueOf(ChronoUnit.DAYS.between(startDate, endDate) + 1),
                reason,
                LeaveRequestStatus.PENDING,
                null,
                null,
                now,
                now
        );
    }

    public void approve(String actorId) {
        ensurePending();
        status = LeaveRequestStatus.APPROVED;
        decidedBy = requireText(actorId, "actor id is required");
        decidedAt = Instant.now();
        updatedAt = decidedAt;
    }

    public void reject(String actorId) {
        ensurePending();
        status = LeaveRequestStatus.REJECTED;
        decidedBy = requireText(actorId, "actor id is required");
        decidedAt = Instant.now();
        updatedAt = decidedAt;
    }

    public void cancel() {
        ensurePending();
        status = LeaveRequestStatus.CANCELLED;
        updatedAt = Instant.now();
    }

    public boolean overlaps(LocalDate start, LocalDate end) {
        return !startDate.isAfter(end) && !endDate.isBefore(start);
    }

    public boolean activeForConflict() {
        return status == LeaveRequestStatus.PENDING || status == LeaveRequestStatus.APPROVED;
    }

    public UUID id() {
        return id;
    }

    public String tenantId() {
        return tenantId;
    }

    public String workerId() {
        return workerId;
    }

    public LeaveType leaveType() {
        return leaveType;
    }

    public LocalDate startDate() {
        return startDate;
    }

    public LocalDate endDate() {
        return endDate;
    }

    public BigDecimal requestedDays() {
        return requestedDays;
    }

    public String reason() {
        return reason;
    }

    public LeaveRequestStatus status() {
        return status;
    }

    public String decidedBy() {
        return decidedBy;
    }

    public Instant decidedAt() {
        return decidedAt;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    private void ensurePending() {
        if (status != LeaveRequestStatus.PENDING) {
            throw new IllegalStateException("leave request is not pending");
        }
    }

    private void validateDates() {
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("end date cannot be before start date");
        }
    }

    private static BigDecimal positive(BigDecimal value, String message) {
        Objects.requireNonNull(value, message);
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
