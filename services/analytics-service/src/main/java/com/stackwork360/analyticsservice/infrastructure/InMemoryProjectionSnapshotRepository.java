package com.stackwork360.analyticsservice.infrastructure;

import com.stackwork360.analyticsservice.domain.ProjectionSnapshot;
import com.stackwork360.analyticsservice.domain.ProjectionSnapshotRepository;
import com.stackwork360.analyticsservice.domain.ProjectionType;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryProjectionSnapshotRepository implements ProjectionSnapshotRepository {
    private final Map<UUID, ProjectionSnapshot> snapshots = new ConcurrentHashMap<>();

    @Override
    public ProjectionSnapshot save(ProjectionSnapshot snapshot) {
        snapshots.put(snapshot.id(), snapshot);
        return snapshot;
    }

    @Override
    public Optional<ProjectionSnapshot> findLatest(String tenantId, ProjectionType type, String scope) {
        return snapshots.values().stream()
                .filter(snapshot -> snapshot.tenantId().equals(tenantId))
                .filter(snapshot -> snapshot.type() == type)
                .filter(snapshot -> snapshot.scope().equals(scope))
                .max(Comparator.comparing(ProjectionSnapshot::generatedAt));
    }

    @Override
    public List<ProjectionSnapshot> findByTenantIdAndType(String tenantId, ProjectionType type) {
        return snapshots.values().stream()
                .filter(snapshot -> snapshot.tenantId().equals(tenantId))
                .filter(snapshot -> snapshot.type() == type)
                .sorted(Comparator.comparing(ProjectionSnapshot::generatedAt).reversed())
                .toList();
    }
}
