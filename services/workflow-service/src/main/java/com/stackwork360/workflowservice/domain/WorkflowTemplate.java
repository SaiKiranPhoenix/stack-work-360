package com.stackwork360.workflowservice.domain;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public record WorkflowTemplate(
        String key,
        String name,
        String description,
        List<WorkflowTemplateStep> steps
) {
    public WorkflowTemplate {
        key = requireText(key, "template key is required");
        name = requireText(name, "template name is required");
        description = description == null ? "" : description.trim();
        steps = List.copyOf(steps == null ? List.of() : steps).stream()
                .sorted(Comparator.comparingInt(WorkflowTemplateStep::sequence))
                .toList();
        if (steps.isEmpty()) {
            throw new IllegalArgumentException("workflow template must contain at least one step");
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
