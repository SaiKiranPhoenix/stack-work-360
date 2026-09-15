package com.stackwork360.attendanceservice.domain;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class AttendanceEntry {
    private static final Duration STANDARD_SHIFT = Duration.ofHours(8);
    private static final Duration LONG_SHIFT = Duration.ofHours(12);
    private static final Duration SHORT_SHIFT = Duration.ofHours(4);

    private final UUID id;
    private final String tenantId;
    private final String workerId;
    private AttendanceEntryType type;
    private Instant checkInAt;
    private Instant checkOutAt;
    private AttendanceEntryStatus status;
    private String location;
    private String correctionReason;
    private final Instant createdAt;
    private Instant updatedAt;

    private AttendanceEntry(
            UUID id,
            String tenantId,
            String workerId,
            AttendanceEntryType type,
            Instant checkInAt,
            Instant checkOutAt,
            AttendanceEntryStatus status,
            String location,
            String correctionReason,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = Objects.requireNonNull(id, "attendance entry id is required");
        this.tenantId = requireText(tenantId, "tenant id is required");
        this.workerId = requireText(workerId, "worker id is required");
        this.type = Objects.requireNonNull(type, "attendance type is required");
        this.checkInAt = Objects.requireNonNull(checkInAt, "check-in time is required");
        this.checkOutAt = checkOutAt;
        this.status = Objects.requireNonNull(status, "attendance status is required");
        this.location = location == null ? "" : location.trim();
        this.correctionReason = correctionReason;
        this.createdAt = Objects.requireNonNull(createdAt, "created at is required");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updated at is required");
        ensureCheckOutAfterCheckIn();
    }

    public static AttendanceEntry checkIn(String tenantId, String workerId, AttendanceEntryType type, Instant checkInAt, String location) {
        Instant now = Instant.now();
        return new AttendanceEntry(UUID.randomUUID(), tenantId, workerId, type, checkInAt, null, AttendanceEntryStatus.OPEN, location, null, now, now);
    }

    public static AttendanceEntry remoteWork(String tenantId, String workerId, Instant startAt, Instant endAt, String location) {
        Instant now = Instant.now();
        return new AttendanceEntry(UUID.randomUUID(), tenantId, workerId, AttendanceEntryType.REMOTE, startAt, endAt, AttendanceEntryStatus.CLOSED, location, null, now, now);
    }

    public void checkOut(Instant checkOutAt) {
        if (status != AttendanceEntryStatus.OPEN) {
            throw new IllegalStateException("only open entries can be checked out");
        }
        this.checkOutAt = Objects.requireNonNull(checkOutAt, "check-out time is required");
        ensureCheckOutAfterCheckIn();
        this.status = AttendanceEntryStatus.CLOSED;
        this.updatedAt = Instant.now();
    }

    public void correct(Instant checkInAt, Instant checkOutAt, AttendanceEntryType type, String location, String reason) {
        this.checkInAt = Objects.requireNonNull(checkInAt, "check-in time is required");
        this.checkOutAt = Objects.requireNonNull(checkOutAt, "check-out time is required");
        this.type = Objects.requireNonNull(type, "attendance type is required");
        this.location = location == null ? "" : location.trim();
        this.correctionReason = requireText(reason, "correction reason is required");
        ensureCheckOutAfterCheckIn();
        this.status = AttendanceEntryStatus.CORRECTED;
        this.updatedAt = Instant.now();
    }

    public Duration workedTime(Instant now) {
        Instant end = checkOutAt == null ? Objects.requireNonNull(now, "current time is required") : checkOutAt;
        return Duration.between(checkInAt, end);
    }

    public Duration overtime() {
        if (checkOutAt == null) {
            return Duration.ZERO;
        }
        Duration worked = workedTime(checkOutAt);
        return worked.compareTo(STANDARD_SHIFT) > 0 ? worked.minus(STANDARD_SHIFT) : Duration.ZERO;
    }

    public AttendanceSummary summary(Instant now) {
        List<AttendanceAnomaly> anomalies = new ArrayList<>();
        Duration worked = workedTime(now);
        if (status == AttendanceEntryStatus.OPEN) {
            anomalies.add(new AttendanceAnomaly(AttendanceAnomalyType.MISSING_CHECK_OUT, "entry has no check-out time"));
        }
        if (worked.compareTo(LONG_SHIFT) > 0) {
            anomalies.add(new AttendanceAnomaly(AttendanceAnomalyType.LONG_SHIFT, "worked time is longer than 12 hours"));
        }
        if (checkOutAt != null && worked.compareTo(SHORT_SHIFT) < 0) {
            anomalies.add(new AttendanceAnomaly(AttendanceAnomalyType.SHORT_SHIFT, "worked time is shorter than 4 hours"));
        }
        if (status == AttendanceEntryStatus.CORRECTED) {
            anomalies.add(new AttendanceAnomaly(AttendanceAnomalyType.MANUAL_CORRECTION, "entry was manually corrected"));
        }
        return new AttendanceSummary(worked, overtime(), List.copyOf(anomalies));
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

    public AttendanceEntryType type() {
        return type;
    }

    public Instant checkInAt() {
        return checkInAt;
    }

    public Instant checkOutAt() {
        return checkOutAt;
    }

    public AttendanceEntryStatus status() {
        return status;
    }

    public String location() {
        return location;
    }

    public String correctionReason() {
        return correctionReason;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    private void ensureCheckOutAfterCheckIn() {
        if (checkOutAt != null && checkOutAt.isBefore(checkInAt)) {
            throw new IllegalArgumentException("check-out time cannot be before check-in time");
        }
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
