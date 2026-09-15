package com.stackwork360.workflowservice.api;

import com.stackwork360.workflowservice.domain.WorkflowInstance;
import com.stackwork360.workflowservice.domain.WorkflowStatus;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record WorkflowInstanceResponse(
        UUID id,
        String tenantId,
        String templateKey,
        String subjectType,
        String subjectId,
        WorkflowStatus status,
        List<WorkflowTaskResponse> tasks,
        Instant createdAt,
        Instant updatedAt
) {
    public static WorkflowInstanceResponse from(WorkflowInstance instance) {
        return new WorkflowInstanceResponse(
                instance.id(),
                instance.tenantId(),
                instance.templateKey(),
                instance.subjectType(),
                instance.subjectId(),
                instance.status(),
                instance.tasks().stream().map(WorkflowTaskResponse::from).toList(),
                instance.createdAt(),
                instance.updatedAt()
        );
    }
}
