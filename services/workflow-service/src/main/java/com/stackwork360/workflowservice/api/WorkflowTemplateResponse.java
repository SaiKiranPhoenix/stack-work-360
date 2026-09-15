package com.stackwork360.workflowservice.api;

import com.stackwork360.workflowservice.domain.WorkflowTaskType;
import com.stackwork360.workflowservice.domain.WorkflowTemplate;
import com.stackwork360.workflowservice.domain.WorkflowTemplateStep;
import java.util.List;

public record WorkflowTemplateResponse(
        String key,
        String name,
        String description,
        List<WorkflowTemplateStepResponse> steps
) {
    public static WorkflowTemplateResponse from(WorkflowTemplate template) {
        return new WorkflowTemplateResponse(
                template.key(),
                template.name(),
                template.description(),
                template.steps().stream().map(WorkflowTemplateStepResponse::from).toList()
        );
    }

    public record WorkflowTemplateStepResponse(
            String key,
            String name,
            WorkflowTaskType type,
            String assigneeRole,
            int sequence
    ) {
        public static WorkflowTemplateStepResponse from(WorkflowTemplateStep step) {
            return new WorkflowTemplateStepResponse(
                    step.key(),
                    step.name(),
                    step.type(),
                    step.assigneeRole(),
                    step.sequence()
            );
        }
    }
}
