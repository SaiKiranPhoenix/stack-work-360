package com.stackwork360.analyticsservice.domain;

import java.util.List;
import java.util.Optional;

public interface ProjectionSnapshotRepository {
    ProjectionSnapshot save(ProjectionSnapshot snapshot);

    Optional<ProjectionSnapshot> findLatest(String tenantId, ProjectionType type, String scope);

    List<ProjectionSnapshot> findByTenantIdAndType(String tenantId, ProjectionType type);
}
