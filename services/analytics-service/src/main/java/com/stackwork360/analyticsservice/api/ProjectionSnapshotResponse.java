package com.stackwork360.analyticsservice.api;

import com.stackwork360.analyticsservice.domain.ProjectionSnapshot;
import com.stackwork360.analyticsservice.domain.ProjectionType;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ProjectionSnapshotResponse(
        UUID id,
        String tenantId,
        ProjectionType type,
        String scope,
        List<MetricResponse> metrics,
        Instant generatedAt
) {
    static ProjectionSnapshotResponse from(ProjectionSnapshot snapshot) {
        return new ProjectionSnapshotResponse(
                snapshot.id(),
                snapshot.tenantId(),
                snapshot.type(),
                snapshot.scope(),
                snapshot.metrics().stream().map(MetricResponse::from).toList(),
                snapshot.generatedAt()
        );
    }
}
