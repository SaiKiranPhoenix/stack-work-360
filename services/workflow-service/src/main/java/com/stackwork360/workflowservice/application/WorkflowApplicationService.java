package com.stackwork360.workflowservice.application;

import com.stackwork360.web.ResourceNotFoundException;
import com.stackwork360.workflowservice.domain.WorkflowInstance;
import com.stackwork360.workflowservice.domain.WorkflowInstanceRepository;
import com.stackwork360.workflowservice.domain.WorkflowTask;
import com.stackwork360.workflowservice.domain.WorkflowTemplate;
import com.stackwork360.workflowservice.domain.WorkflowTemplateRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class WorkflowApplicationService {
    private final WorkflowTemplateRepository templateRepository;
    private final WorkflowInstanceRepository instanceRepository;

    public WorkflowApplicationService(
            WorkflowTemplateRepository templateRepository,
            WorkflowInstanceRepository instanceRepository
    ) {
        this.templateRepository = templateRepository;
        this.instanceRepository = instanceRepository;
    }

    public List<WorkflowTemplate> templates() {
        return templateRepository.findAll();
    }

    public WorkflowInstance start(StartWorkflowCommand command) {
        WorkflowTemplate template = templateRepository.findByKey(command.templateKey())
                .orElseThrow(() -> new ResourceNotFoundException("workflow template not found"));
        WorkflowInstance instance = WorkflowInstance.start(
                command.tenantId(),
                template,
                command.subjectType(),
                command.subjectId()
        );
        return instanceRepository.save(instance);
    }

    public WorkflowTask approveTask(UUID workflowId, UUID taskId, String actorId) {
        WorkflowInstance instance = get(workflowId);
        WorkflowTask task = instance.approveTask(taskId, actorId);
        instanceRepository.save(instance);
        return task;
    }

    public WorkflowTask completeTask(UUID workflowId, UUID taskId, String actorId) {
        WorkflowInstance instance = get(workflowId);
        WorkflowTask task = instance.completeTask(taskId, actorId);
        instanceRepository.save(instance);
        return task;
    }

    public WorkflowTask rejectTask(UUID workflowId, UUID taskId, String actorId) {
        WorkflowInstance instance = get(workflowId);
        WorkflowTask task = instance.rejectTask(taskId, actorId);
        instanceRepository.save(instance);
        return task;
    }

    public WorkflowInstance cancel(UUID workflowId) {
        WorkflowInstance instance = get(workflowId);
        instance.cancel();
        return instanceRepository.save(instance);
    }

    public WorkflowInstance get(UUID workflowId) {
        return instanceRepository.findById(workflowId)
                .orElseThrow(() -> new ResourceNotFoundException("workflow instance not found"));
    }

    public List<WorkflowInstance> list(String tenantId) {
        return instanceRepository.findByTenantId(tenantId);
    }
}
