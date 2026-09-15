package com.stackwork360.workflowservice.application;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.stackwork360.workflowservice.domain.WorkflowInstance;
import com.stackwork360.workflowservice.domain.WorkflowStatus;
import com.stackwork360.workflowservice.infrastructure.InMemoryWorkflowInstanceRepository;
import com.stackwork360.workflowservice.infrastructure.InMemoryWorkflowTemplateRepository;
import org.junit.jupiter.api.Test;

class WorkflowApplicationServiceTest {
    private final WorkflowApplicationService service = new WorkflowApplicationService(
            new InMemoryWorkflowTemplateRepository(),
            new InMemoryWorkflowInstanceRepository()
    );

    @Test
    void startsSeededOnboardingWorkflow() {
        WorkflowInstance instance = service.start(new StartWorkflowCommand(
                "tenant-1",
                "employee-onboarding",
                "worker",
                "worker-1"
        ));

        assertEquals("employee-onboarding", instance.templateKey());
        assertEquals(WorkflowStatus.RUNNING, instance.status());
        assertEquals(3, instance.tasks().size());
    }

    @Test
    void cancelsRunningWorkflow() {
        WorkflowInstance instance = service.start(new StartWorkflowCommand(
                "tenant-1",
                "role-change",
                "worker",
                "worker-1"
        ));

        WorkflowInstance cancelled = service.cancel(instance.id());

        assertEquals(WorkflowStatus.CANCELLED, cancelled.status());
    }
}
