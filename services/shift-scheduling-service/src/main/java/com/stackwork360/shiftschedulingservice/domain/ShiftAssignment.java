package com.stackwork360.shiftschedulingservice.domain;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.UUID;

public final class ShiftAssignment {
    private final UUID id;
    private final String tenantId;
    private final UUID templateId;
    private String workerId;
    private final LocalDate shiftDate;
    private final Instant startsAt;
    private final Instant endsAt;
    private final Instant createdAt;

    private ShiftAssignment(UUID id, String tenantId, UUID templateId, String workerId, LocalDate shiftDate, Instant startsAt, Instant endsAt, Instant createdAt) {
        this.id = Objects.requireNonNull(id, "assignment id is required");
        this.tenantId = requireText(tenantId, "tenant id is required");
        this.templateId = Objects.requireNonNull(templateId, "template id is required");
        this.workerId = requireText(workerId, "worker id is required");
        this.shiftDate = Objects.requireNonNull(shiftDate, "shift date is required");
        this.startsAt = Objects.requireNonNull(startsAt, "starts at is required");
        this.endsAt = Objects.requireNonNull(endsAt, "ends at is required");
        if (!endsAt.isAfter(startsAt)) {
            throw new IllegalArgumentException("assignment end must be after start");
        }
        this.createdAt = Objects.requireNonNull(createdAt, "created at is required");
    }

    public static ShiftAssignment assign(String tenantId, ShiftTemplate template, String workerId, LocalDate shiftDate) {
        Instant startsAt = shiftDate.atTime(template.startTime()).toInstant(ZoneOffset.UTC);
        Instant endsAt = startsAt.plus(template.duration());
        return new ShiftAssignment(UUID.randomUUID(), tenantId, template.id(), workerId, shiftDate, startsAt, endsAt, Instant.now());
    }

    public boolean conflictsWith(ShiftAssignment other) {
        return tenantId.equals(other.tenantId())
                && workerId.equals(other.workerId())
                && startsAt.isBefore(other.endsAt())
                && endsAt.isAfter(other.startsAt());
    }

    public void reassignTo(String workerId) {
        this.workerId = requireText(workerId, "worker id is required");
    }

    public UUID id() {
        return id;
    }

    public String tenantId() {
        return tenantId;
    }

    public UUID templateId() {
        return templateId;
    }

    public String workerId() {
        return workerId;
    }

    public LocalDate shiftDate() {
        return shiftDate;
    }

    public Instant startsAt() {
        return startsAt;
    }

    public Instant endsAt() {
        return endsAt;
    }

    public Instant createdAt() {
        return createdAt;
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
