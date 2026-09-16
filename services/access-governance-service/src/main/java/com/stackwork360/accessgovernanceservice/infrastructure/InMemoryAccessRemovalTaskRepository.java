package com.stackwork360.accessgovernanceservice.infrastructure;

import com.stackwork360.accessgovernanceservice.domain.AccessRemovalTask;
import com.stackwork360.accessgovernanceservice.domain.AccessRemovalTaskRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryAccessRemovalTaskRepository implements AccessRemovalTaskRepository {
    private final Map<UUID, AccessRemovalTask> tasks = new ConcurrentHashMap<>();

    public AccessRemovalTask save(AccessRemovalTask task) {
        tasks.put(task.id(), task);
        return task;
    }

    public List<AccessRemovalTask> findByTenantId(String tenantId) {
        return tasks.values().stream()
                .filter(task -> task.tenantId().equals(tenantId))
                .sorted(Comparator.comparing(AccessRemovalTask::createdAt).reversed())
                .toList();
    }
}
