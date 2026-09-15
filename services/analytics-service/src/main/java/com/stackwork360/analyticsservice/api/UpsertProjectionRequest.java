package com.stackwork360.analyticsservice.api;

import com.stackwork360.analyticsservice.application.UpsertProjectionCommand;
import com.stackwork360.analyticsservice.domain.ProjectionType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record UpsertProjectionRequest(
        @NotNull ProjectionType type,
        String scope,
        @NotEmpty List<@Valid MetricRequest> metrics
) {
    UpsertProjectionCommand toCommand(String tenantId) {
        return new UpsertProjectionCommand(
                tenantId,
                type,
                scope,
                metrics.stream().map(MetricRequest::toCommand).toList()
        );
    }
}
