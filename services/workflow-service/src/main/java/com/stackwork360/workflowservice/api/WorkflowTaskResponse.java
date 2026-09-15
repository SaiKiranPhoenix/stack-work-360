package com.stackwork360.workflowservice.api;

import com.stackwork360.workflowservice.domain.TaskStatus;
import com.stackwork360.workflowservice.domain.WorkflowTask;
import com.stackwork360.workflowservice.domain.WorkflowTaskType;
import java.time.Instant;
import java.util.UUID;

public record WorkflowTaskResponse(
        UUID id,
        String stepKey,
        String name,
        WorkflowTaskType type,
        String assigneeRole,
        int sequence,
        TaskStatus status,
        String actedBy,
        Instant actedAt
) {
    public static WorkflowTaskResponse from(WorkflowTask task) {
        return new WorkflowTaskResponse(
                task.id(),
                task.stepKey(),
                task.name(),
                task.type(),
                task.assigneeRole(),
                task.sequence(),
                task.status(),
                task.actedBy(),
                task.actedAt()
        );
    }
}
