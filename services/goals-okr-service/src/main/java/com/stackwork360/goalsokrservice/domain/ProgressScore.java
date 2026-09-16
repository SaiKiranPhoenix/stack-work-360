package com.stackwork360.goalsokrservice.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record ProgressScore(BigDecimal value) {
    public ProgressScore {
        value = value == null ? BigDecimal.ZERO : value.setScale(2, RoundingMode.HALF_UP);
        if (value.compareTo(BigDecimal.ZERO) < 0 || value.compareTo(new BigDecimal("100.00")) > 0) {
            throw new IllegalArgumentException("progress score must be between 0 and 100");
        }
    }

    public static ProgressScore zero() {
        return new ProgressScore(BigDecimal.ZERO);
    }
}
