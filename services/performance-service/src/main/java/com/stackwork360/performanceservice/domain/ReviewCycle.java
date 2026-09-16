package com.stackwork360.performanceservice.domain;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public final class ReviewCycle {
    private final UUID id;
    private final String tenantId;
    private final String name;
    private final LocalDate startsOn;
    private final LocalDate endsOn;
    private ReviewCycleStatus status;

    public ReviewCycle(UUID id, String tenantId, String name, LocalDate startsOn, LocalDate endsOn, ReviewCycleStatus status) {
        this.id = id == null ? UUID.randomUUID() : id;
        this.tenantId = requireText(tenantId, "tenant id is required");
        this.name = requireText(name, "cycle name is required");
        this.startsOn = Objects.requireNonNull(startsOn, "start date is required");
        this.endsOn = Objects.requireNonNull(endsOn, "end date is required");
        if (endsOn.isBefore(startsOn)) {
            throw new IllegalArgumentException("cycle end date cannot be before start date");
        }
        this.status = Objects.requireNonNull(status, "cycle status is required");
    }

    public void activate() {
        if (status != ReviewCycleStatus.DRAFT) {
            throw new IllegalStateException("only draft cycles can be activated");
        }
        status = ReviewCycleStatus.ACTIVE;
    }

    public void startCalibration() {
        if (status != ReviewCycleStatus.ACTIVE) {
            throw new IllegalStateException("only active cycles can enter calibration");
        }
        status = ReviewCycleStatus.CALIBRATION;
    }

    public void close() {
        if (status != ReviewCycleStatus.CALIBRATION) {
            throw new IllegalStateException("only calibration cycles can be closed");
        }
        status = ReviewCycleStatus.CLOSED;
    }

    public UUID id() { return id; }
    public String tenantId() { return tenantId; }
    public String name() { return name; }
    public LocalDate startsOn() { return startsOn; }
    public LocalDate endsOn() { return endsOn; }
    public ReviewCycleStatus status() { return status; }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
