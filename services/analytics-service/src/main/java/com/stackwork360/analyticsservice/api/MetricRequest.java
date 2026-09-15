package com.stackwork360.analyticsservice.api;

import com.stackwork360.analyticsservice.application.MetricCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record MetricRequest(
        @NotBlank String key,
        @NotNull BigDecimal value
) {
    MetricCommand toCommand() {
        return new MetricCommand(key, value);
    }
}
