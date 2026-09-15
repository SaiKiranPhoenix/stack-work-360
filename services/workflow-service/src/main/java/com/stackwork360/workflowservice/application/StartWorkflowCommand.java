package com.stackwork360.workflowservice.application;

public record StartWorkflowCommand(
        String tenantId,
        String templateKey,
        String subjectType,
        String subjectId
) {
}
