package com.stackwork360.workflowservice.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class WorkflowTask {
    private final UUID id;
    private final String stepKey;
    private final String name;
    private final WorkflowTaskType type;
    private final String assigneeRole;
    private final int sequence;
    private TaskStatus status;
    private String actedBy;
    private Instant actedAt;

    private WorkflowTask(
            UUID id,
            String stepKey,
            String name,
            WorkflowTaskType type,
            String assigneeRole,
            int sequence,
            TaskStatus status,
            String actedBy,
            Instant actedAt
    ) {
        this.id = Objects.requireNonNull(id, "task id is required");
        this.stepKey = requireText(stepKey, "step key is required");
        this.name = requireText(name, "task name is required");
        this.type = Objects.requireNonNull(type, "task type is required");
        this.assigneeRole = requireText(assigneeRole, "assignee role is required");
        this.sequence = sequence;
        this.status = Objects.requireNonNull(status, "task status is required");
        this.actedBy = actedBy;
        this.actedAt = actedAt;
    }

    public static WorkflowTask open(
            String stepKey,
            String name,
            WorkflowTaskType type,
            String assigneeRole,
            int sequence
    ) {
        return new WorkflowTask(
                UUID.randomUUID(),
                stepKey,
                name,
                type,
                assigneeRole,
                sequence,
                TaskStatus.OPEN,
                null,
                null
        );
    }

    public void approve(String actorId) {
        ensureOpen();
        if (type != WorkflowTaskType.APPROVAL) {
            throw new IllegalStateException("only approval tasks can be approved");
        }
        close(TaskStatus.APPROVED, actorId);
    }

    public void complete(String actorId) {
        ensureOpen();
        if (type == WorkflowTaskType.APPROVAL) {
            throw new IllegalStateException("approval task must be approved or rejected");
        }
        close(TaskStatus.COMPLETED, actorId);
    }

    public void reject(String actorId) {
        ensureOpen();
        if (type != WorkflowTaskType.APPROVAL) {
            throw new IllegalStateException("only approval tasks can be rejected");
        }
        close(TaskStatus.REJECTED, actorId);
    }

    public void cancel() {
        ensureOpen();
        status = TaskStatus.CANCELLED;
        actedAt = Instant.now();
    }

    public UUID id() {
        return id;
    }

    public String stepKey() {
        return stepKey;
    }

    public String name() {
        return name;
    }

    public WorkflowTaskType type() {
        return type;
    }

    public String assigneeRole() {
        return assigneeRole;
    }

    public int sequence() {
        return sequence;
    }

    public TaskStatus status() {
        return status;
    }

    public String actedBy() {
        return actedBy;
    }

    public Instant actedAt() {
        return actedAt;
    }

    public boolean open() {
        return status == TaskStatus.OPEN;
    }

    private void close(TaskStatus status, String actorId) {
        this.status = status;
        this.actedBy = requireText(actorId, "actor id is required");
        this.actedAt = Instant.now();
    }

    private void ensureOpen() {
        if (!open()) {
            throw new IllegalStateException("task is already closed");
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
