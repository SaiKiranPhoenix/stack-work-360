package com.stackwork360.workflowservice.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class WorkflowInstance {
    private final UUID id;
    private final String tenantId;
    private final String templateKey;
    private final String subjectType;
    private final String subjectId;
    private WorkflowStatus status;
    private final List<WorkflowTask> tasks;
    private final Instant createdAt;
    private Instant updatedAt;

    private WorkflowInstance(
            UUID id,
            String tenantId,
            String templateKey,
            String subjectType,
            String subjectId,
            WorkflowStatus status,
            List<WorkflowTask> tasks,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = Objects.requireNonNull(id, "workflow id is required");
        this.tenantId = requireText(tenantId, "tenant id is required");
        this.templateKey = requireText(templateKey, "template key is required");
        this.subjectType = requireText(subjectType, "subject type is required");
        this.subjectId = requireText(subjectId, "subject id is required");
        this.status = Objects.requireNonNull(status, "workflow status is required");
        this.tasks = new ArrayList<>(tasks);
        this.createdAt = Objects.requireNonNull(createdAt, "created at is required");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updated at is required");
    }

    public static WorkflowInstance start(
            String tenantId,
            WorkflowTemplate template,
            String subjectType,
            String subjectId
    ) {
        Instant now = Instant.now();
        List<WorkflowTask> tasks = template.steps().stream()
                .map(step -> WorkflowTask.open(step.key(), step.name(), step.type(), step.assigneeRole(), step.sequence()))
                .toList();
        return new WorkflowInstance(
                UUID.randomUUID(),
                tenantId,
                template.key(),
                subjectType,
                subjectId,
                WorkflowStatus.RUNNING,
                tasks,
                now,
                now
        );
    }

    public WorkflowTask approveTask(UUID taskId, String actorId) {
        ensureRunning();
        WorkflowTask task = task(taskId);
        task.approve(actorId);
        completeIfAllTerminal();
        updatedAt = Instant.now();
        return task;
    }

    public WorkflowTask completeTask(UUID taskId, String actorId) {
        ensureRunning();
        WorkflowTask task = task(taskId);
        task.complete(actorId);
        completeIfAllTerminal();
        updatedAt = Instant.now();
        return task;
    }

    public WorkflowTask rejectTask(UUID taskId, String actorId) {
        ensureRunning();
        WorkflowTask task = task(taskId);
        task.reject(actorId);
        status = WorkflowStatus.REJECTED;
        updatedAt = Instant.now();
        return task;
    }

    public void cancel() {
        if (status == WorkflowStatus.COMPLETED || status == WorkflowStatus.REJECTED) {
            throw new IllegalStateException("terminal workflow cannot be cancelled");
        }
        tasks.stream()
                .filter(WorkflowTask::open)
                .forEach(WorkflowTask::cancel);
        status = WorkflowStatus.CANCELLED;
        updatedAt = Instant.now();
    }

    public UUID id() {
        return id;
    }

    public String tenantId() {
        return tenantId;
    }

    public String templateKey() {
        return templateKey;
    }

    public String subjectType() {
        return subjectType;
    }

    public String subjectId() {
        return subjectId;
    }

    public WorkflowStatus status() {
        return status;
    }

    public List<WorkflowTask> tasks() {
        return tasks.stream()
                .sorted(Comparator.comparingInt(WorkflowTask::sequence))
                .toList();
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    private WorkflowTask task(UUID taskId) {
        return tasks.stream()
                .filter(task -> task.id().equals(taskId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("workflow task not found"));
    }

    private void ensureRunning() {
        if (status != WorkflowStatus.RUNNING) {
            throw new IllegalStateException("workflow is not running");
        }
    }

    private void completeIfAllTerminal() {
        boolean allClosed = tasks.stream().noneMatch(WorkflowTask::open);
        if (allClosed) {
            status = WorkflowStatus.COMPLETED;
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
