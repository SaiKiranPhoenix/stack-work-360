package com.stackwork360.workflowservice.domain;

import java.util.Objects;

public record WorkflowTemplateStep(
        String key,
        String name,
        WorkflowTaskType type,
        String assigneeRole,
        int sequence
) {
    public WorkflowTemplateStep {
        key = requireText(key, "step key is required");
        name = requireText(name, "step name is required");
        type = Objects.requireNonNull(type, "step type is required");
        assigneeRole = requireText(assigneeRole, "assignee role is required");
        if (sequence < 1) {
            throw new IllegalArgumentException("step sequence must be positive");
        }
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
