package com.stackwork360.workflowservice.api;

import com.stackwork360.common.CorrelationIds;
import com.stackwork360.workflowservice.application.StartWorkflowCommand;
import com.stackwork360.workflowservice.application.WorkflowApplicationService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/workflows/v1")
public class WorkflowController {
    private final WorkflowApplicationService workflowApplicationService;

    public WorkflowController(WorkflowApplicationService workflowApplicationService) {
        this.workflowApplicationService = workflowApplicationService;
    }

    @GetMapping("/templates")
    public List<WorkflowTemplateResponse> templates() {
        return workflowApplicationService.templates().stream()
                .map(WorkflowTemplateResponse::from)
                .toList();
    }

    @PostMapping("/instances")
    public ResponseEntity<WorkflowInstanceResponse> start(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @Valid @RequestBody StartWorkflowRequest request
    ) {
        WorkflowInstanceResponse response = WorkflowInstanceResponse.from(workflowApplicationService.start(new StartWorkflowCommand(
                tenantId,
                request.templateKey(),
                request.subjectType(),
                request.subjectId()
        )));
        return ResponseEntity.created(URI.create("/api/workflows/v1/instances/" + response.id()))
                .body(response);
    }

    @GetMapping("/instances")
    public List<WorkflowInstanceResponse> list(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return workflowApplicationService.list(tenantId).stream()
                .map(WorkflowInstanceResponse::from)
                .toList();
    }

    @GetMapping("/instances/{workflowId}")
    public WorkflowInstanceResponse get(@PathVariable UUID workflowId) {
        return WorkflowInstanceResponse.from(workflowApplicationService.get(workflowId));
    }

    @PatchMapping("/instances/{workflowId}/tasks/{taskId}/approve")
    public WorkflowTaskResponse approve(
            @PathVariable UUID workflowId,
            @PathVariable UUID taskId,
            @Valid @RequestBody TaskActionRequest request
    ) {
        return WorkflowTaskResponse.from(workflowApplicationService.approveTask(workflowId, taskId, request.actorId()));
    }

    @PatchMapping("/instances/{workflowId}/tasks/{taskId}/complete")
    public WorkflowTaskResponse complete(
            @PathVariable UUID workflowId,
            @PathVariable UUID taskId,
            @Valid @RequestBody TaskActionRequest request
    ) {
        return WorkflowTaskResponse.from(workflowApplicationService.completeTask(workflowId, taskId, request.actorId()));
    }

    @PatchMapping("/instances/{workflowId}/tasks/{taskId}/reject")
    public WorkflowTaskResponse reject(
            @PathVariable UUID workflowId,
            @PathVariable UUID taskId,
            @Valid @RequestBody TaskActionRequest request
    ) {
        return WorkflowTaskResponse.from(workflowApplicationService.rejectTask(workflowId, taskId, request.actorId()));
    }

    @PatchMapping("/instances/{workflowId}/cancel")
    public WorkflowInstanceResponse cancel(@PathVariable UUID workflowId) {
        return WorkflowInstanceResponse.from(workflowApplicationService.cancel(workflowId));
    }
}
