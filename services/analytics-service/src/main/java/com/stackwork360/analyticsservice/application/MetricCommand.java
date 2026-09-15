package com.stackwork360.analyticsservice.application;

import java.math.BigDecimal;

public record MetricCommand(
        String key,
        BigDecimal value
) {
}
