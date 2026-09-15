package com.stackwork360.workflowservice.domain;

import java.util.List;
import java.util.Optional;

public interface WorkflowTemplateRepository {
    WorkflowTemplate save(WorkflowTemplate template);

    Optional<WorkflowTemplate> findByKey(String key);

    List<WorkflowTemplate> findAll();
}
