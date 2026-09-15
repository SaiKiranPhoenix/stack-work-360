package com.stackwork360.workflowservice.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkflowInstanceRepository {
    WorkflowInstance save(WorkflowInstance instance);

    Optional<WorkflowInstance> findById(UUID id);

    List<WorkflowInstance> findByTenantId(String tenantId);
}
