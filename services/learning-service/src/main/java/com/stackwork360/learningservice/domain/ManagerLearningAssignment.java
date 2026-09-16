package com.stackwork360.learningservice.domain;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public final class ManagerLearningAssignment {
    private final UUID id;
    private final String tenantId;
    private final String workerId;
    private final String managerId;
    private final UUID resourceId;
    private final LocalDate dueOn;
    private AssignmentStatus status;
    private final Instant assignedAt;
    private Instant updatedAt;

    public ManagerLearningAssignment(UUID id, String tenantId, String workerId, String managerId, UUID resourceId, LocalDate dueOn, AssignmentStatus status, Instant assignedAt, Instant updatedAt) {
        this.id = id == null ? UUID.randomUUID() : id;
        this.tenantId = requireText(tenantId, "tenant id is required");
        this.workerId = requireText(workerId, "worker id is required");
        this.managerId = requireText(managerId, "manager id is required");
        this.resourceId = Objects.requireNonNull(resourceId, "resource id is required");
        this.dueOn = Objects.requireNonNull(dueOn, "due date is required");
        this.status = Objects.requireNonNull(status, "assignment status is required");
        this.assignedAt = assignedAt == null ? Instant.now() : assignedAt;
        this.updatedAt = updatedAt == null ? this.assignedAt : updatedAt;
    }

    public void complete() {
        if (status == AssignmentStatus.CANCELLED) {
            throw new IllegalStateException("cancelled assignments cannot be completed");
        }
        status = AssignmentStatus.COMPLETED;
        updatedAt = Instant.now();
    }

    public UUID id() { return id; }
    public String tenantId() { return tenantId; }
    public String workerId() { return workerId; }
    public String managerId() { return managerId; }
    public UUID resourceId() { return resourceId; }
    public LocalDate dueOn() { return dueOn; }
    public AssignmentStatus status() { return status; }
    public Instant assignedAt() { return assignedAt; }
    public Instant updatedAt() { return updatedAt; }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
