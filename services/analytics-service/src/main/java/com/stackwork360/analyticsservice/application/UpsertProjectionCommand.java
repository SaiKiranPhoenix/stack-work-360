package com.stackwork360.analyticsservice.application;

import com.stackwork360.analyticsservice.domain.ProjectionType;
import java.util.List;

public record UpsertProjectionCommand(
        String tenantId,
        ProjectionType type,
        String scope,
        List<MetricCommand> metrics
) {
}
