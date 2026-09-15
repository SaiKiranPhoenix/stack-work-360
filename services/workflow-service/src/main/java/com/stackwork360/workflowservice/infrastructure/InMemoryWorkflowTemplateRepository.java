package com.stackwork360.workflowservice.infrastructure;

import com.stackwork360.workflowservice.domain.WorkflowTaskType;
import com.stackwork360.workflowservice.domain.WorkflowTemplate;
import com.stackwork360.workflowservice.domain.WorkflowTemplateRepository;
import com.stackwork360.workflowservice.domain.WorkflowTemplateStep;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryWorkflowTemplateRepository implements WorkflowTemplateRepository {
    private final ConcurrentMap<String, WorkflowTemplate> templatesByKey = new ConcurrentHashMap<>();

    public InMemoryWorkflowTemplateRepository() {
        save(new WorkflowTemplate(
                "employee-onboarding",
                "Employee onboarding",
                "Cross-functional onboarding workflow for new workers",
                List.of(
                        new WorkflowTemplateStep("hr-profile-check", "Verify HR profile", WorkflowTaskType.TASK, "HR_OPS", 1),
                        new WorkflowTemplateStep("it-access", "Prepare app access", WorkflowTaskType.TASK, "IT_ADMIN", 2),
                        new WorkflowTemplateStep("manager-approval", "Manager onboarding approval", WorkflowTaskType.APPROVAL, "MANAGER", 3)
                )
        ));
        save(new WorkflowTemplate(
                "employee-offboarding",
                "Employee offboarding",
                "Access, payroll, document, and asset closure workflow",
                List.of(
                        new WorkflowTemplateStep("access-review", "Remove access", WorkflowTaskType.TASK, "SECURITY_ADMIN", 1),
                        new WorkflowTemplateStep("asset-return", "Confirm asset return", WorkflowTaskType.TASK, "IT_ADMIN", 2),
                        new WorkflowTemplateStep("final-approval", "Final HR approval", WorkflowTaskType.APPROVAL, "HR_OPS", 3)
                )
        ));
        save(new WorkflowTemplate(
                "role-change",
                "Role change",
                "Role, manager, compensation, and access change workflow",
                List.of(
                        new WorkflowTemplateStep("manager-approval", "Manager approval", WorkflowTaskType.APPROVAL, "MANAGER", 1),
                        new WorkflowTemplateStep("access-review", "Review changed access", WorkflowTaskType.TASK, "SECURITY_ADMIN", 2),
                        new WorkflowTemplateStep("hr-confirmation", "Confirm HR records", WorkflowTaskType.TASK, "HR_OPS", 3)
                )
        ));
    }

    @Override
    public WorkflowTemplate save(WorkflowTemplate template) {
        templatesByKey.put(template.key(), template);
        return template;
    }

    @Override
    public Optional<WorkflowTemplate> findByKey(String key) {
        return Optional.ofNullable(templatesByKey.get(key));
    }

    @Override
    public List<WorkflowTemplate> findAll() {
        return templatesByKey.values().stream()
                .sorted(Comparator.comparing(WorkflowTemplate::key))
                .toList();
    }
}
