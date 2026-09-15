package com.stackwork360.workflowservice.infrastructure;

import com.stackwork360.workflowservice.domain.WorkflowInstance;
import com.stackwork360.workflowservice.domain.WorkflowInstanceRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryWorkflowInstanceRepository implements WorkflowInstanceRepository {
    private final ConcurrentMap<UUID, WorkflowInstance> instancesById = new ConcurrentHashMap<>();

    @Override
    public WorkflowInstance save(WorkflowInstance instance) {
        instancesById.put(instance.id(), instance);
        return instance;
    }

    @Override
    public Optional<WorkflowInstance> findById(UUID id) {
        return Optional.ofNullable(instancesById.get(id));
    }

    @Override
    public List<WorkflowInstance> findByTenantId(String tenantId) {
        return instancesById.values().stream()
                .filter(instance -> instance.tenantId().equals(tenantId))
                .sorted(Comparator.comparing(WorkflowInstance::createdAt))
                .toList();
    }
}
