package com.stackwork360.workflowservice.api;

import jakarta.validation.constraints.NotBlank;

public record StartWorkflowRequest(
        @NotBlank
        String templateKey,

        @NotBlank
        String subjectType,

        @NotBlank
        String subjectId
) {
}
