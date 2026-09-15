package com.stackwork360.analyticsservice.api;

import com.stackwork360.analyticsservice.domain.AnalyticsMetric;
import java.math.BigDecimal;

public record MetricResponse(
        String key,
        BigDecimal value
) {
    static MetricResponse from(AnalyticsMetric metric) {
        return new MetricResponse(metric.key(), metric.value());
    }
}
