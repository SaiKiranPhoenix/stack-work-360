package com.stackwork360.leaveservice.domain;

import java.math.BigDecimal;
import java.util.Objects;

public final class LeaveBalance {
    private final String tenantId;
    private final String workerId;
    private final LeaveType leaveType;
    private BigDecimal availableDays;
    private BigDecimal reservedDays;

    public LeaveBalance(String tenantId, String workerId, LeaveType leaveType, BigDecimal availableDays) {
        this.tenantId = requireText(tenantId, "tenant id is required");
        this.workerId = requireText(workerId, "worker id is required");
        this.leaveType = Objects.requireNonNull(leaveType, "leave type is required");
        this.availableDays = nonNegative(availableDays, "available days must be non-negative");
        this.reservedDays = BigDecimal.ZERO;
    }

    public void reserve(BigDecimal days) {
        BigDecimal requested = positive(days, "reserved days must be positive");
        if (availableDays.subtract(reservedDays).compareTo(requested) < 0) {
            throw new IllegalStateException("insufficient leave balance");
        }
        reservedDays = reservedDays.add(requested);
    }

    public void consumeReserved(BigDecimal days) {
        BigDecimal requested = positive(days, "consumed days must be positive");
        if (reservedDays.compareTo(requested) < 0) {
            throw new IllegalStateException("reserved balance is insufficient");
        }
        reservedDays = reservedDays.subtract(requested);
        availableDays = availableDays.subtract(requested);
    }

    public void release(BigDecimal days) {
        BigDecimal requested = positive(days, "released days must be positive");
        if (reservedDays.compareTo(requested) < 0) {
            throw new IllegalStateException("reserved balance is insufficient");
        }
        reservedDays = reservedDays.subtract(requested);
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

    public BigDecimal availableDays() {
        return availableDays;
    }

    public BigDecimal reservedDays() {
        return reservedDays;
    }

    public BigDecimal remainingDays() {
        return availableDays.subtract(reservedDays);
    }

    private static BigDecimal positive(BigDecimal value, String message) {
        Objects.requireNonNull(value, message);
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    private static BigDecimal nonNegative(BigDecimal value, String message) {
        Objects.requireNonNull(value, message);
        if (value.compareTo(BigDecimal.ZERO) < 0) {
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
