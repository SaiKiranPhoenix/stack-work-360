package com.stackwork360.analyticsservice.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record AnalyticsMetric(
        String key,
        BigDecimal value
) {
    public AnalyticsMetric {
        key = requireText(key, "metric key is required");
        Objects.requireNonNull(value, "metric value is required");
        value = value.setScale(2, RoundingMode.HALF_UP);
    }

    public static AnalyticsMetric of(String key, long value) {
        return new AnalyticsMetric(key, BigDecimal.valueOf(value));
    }

    public static AnalyticsMetric of(String key, String value) {
        return new AnalyticsMetric(key, new BigDecimal(value));
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
