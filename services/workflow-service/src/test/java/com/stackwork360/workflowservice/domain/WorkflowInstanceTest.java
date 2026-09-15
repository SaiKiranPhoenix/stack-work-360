package com.stackwork360.workflowservice.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;

class WorkflowInstanceTest {
    @Test
    void startsWorkflowWithTasksFromTemplate() {
        WorkflowInstance instance = WorkflowInstance.start("tenant-1", template(), "worker", "worker-1");

        assertEquals(WorkflowStatus.RUNNING, instance.status());
        assertEquals(2, instance.tasks().size());
    }

    @Test
    void completesWorkflowWhenAllTasksAreClosed() {
        WorkflowInstance instance = WorkflowInstance.start("tenant-1", template(), "worker", "worker-1");

        instance.completeTask(instance.tasks().get(0).id(), "hr-1");
        instance.approveTask(instance.tasks().get(1).id(), "manager-1");

        assertEquals(WorkflowStatus.COMPLETED, instance.status());
    }

    @Test
    void rejectsWorkflowWhenApprovalIsRejected() {
        WorkflowInstance instance = WorkflowInstance.start("tenant-1", template(), "worker", "worker-1");

        instance.rejectTask(instance.tasks().get(1).id(), "manager-1");

        assertEquals(WorkflowStatus.REJECTED, instance.status());
    }

    @Test
    void rejectsApprovalOnNonApprovalTask() {
        WorkflowInstance instance = WorkflowInstance.start("tenant-1", template(), "worker", "worker-1");

        assertThrows(IllegalStateException.class, () -> instance.approveTask(instance.tasks().get(0).id(), "hr-1"));
    }

    private static WorkflowTemplate template() {
        return new WorkflowTemplate(
                "test-workflow",
                "Test workflow",
                "Test",
                List.of(
                        new WorkflowTemplateStep("task-1", "Task one", WorkflowTaskType.TASK, "HR_OPS", 1),
                        new WorkflowTemplateStep("approval-1", "Approval one", WorkflowTaskType.APPROVAL, "MANAGER", 2)
                )
        );
    }
}
