package com.stackwork360.goalsokrservice.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.UUID;

public final class KeyResult {
    private final UUID id;
    private final String title;
    private final KeyResultType type;
    private final BigDecimal startValue;
    private final BigDecimal targetValue;
    private BigDecimal currentValue;

    public KeyResult(UUID id, String title, KeyResultType type, BigDecimal startValue, BigDecimal targetValue, BigDecimal currentValue) {
        this.id = id == null ? UUID.randomUUID() : id;
        this.title = requireText(title, "key result title is required");
        this.type = Objects.requireNonNull(type, "key result type is required");
        this.startValue = scale(Objects.requireNonNull(startValue, "start value is required"));
        this.targetValue = scale(Objects.requireNonNull(targetValue, "target value is required"));
        if (this.targetValue.compareTo(this.startValue) < 0) {
            throw new IllegalArgumentException("target value cannot be less than start value");
        }
        this.currentValue = scale(currentValue == null ? this.startValue : currentValue);
    }

    public void update(BigDecimal value) {
        currentValue = scale(Objects.requireNonNull(value, "current value is required"));
    }

    public ProgressScore progress() {
        if (type == KeyResultType.BOOLEAN) {
            return new ProgressScore(currentValue.compareTo(BigDecimal.ONE) >= 0 ? new BigDecimal("100") : BigDecimal.ZERO);
        }
        BigDecimal denominator = targetValue.subtract(startValue);
        if (denominator.compareTo(BigDecimal.ZERO) == 0) {
            return new ProgressScore(new BigDecimal("100"));
        }
        BigDecimal raw = currentValue.subtract(startValue)
                .multiply(new BigDecimal("100"))
                .divide(denominator, 2, RoundingMode.HALF_UP);
        BigDecimal clamped = raw.max(BigDecimal.ZERO).min(new BigDecimal("100"));
        return new ProgressScore(clamped);
    }

    public UUID id() { return id; }
    public String title() { return title; }
    public KeyResultType type() { return type; }
    public BigDecimal startValue() { return startValue; }
    public BigDecimal targetValue() { return targetValue; }
    public BigDecimal currentValue() { return currentValue; }

    private static BigDecimal scale(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP);
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
