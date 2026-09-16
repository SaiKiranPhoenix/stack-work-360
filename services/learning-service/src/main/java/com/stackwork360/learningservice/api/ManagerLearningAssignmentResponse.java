package com.stackwork360.learningservice.api;

import com.stackwork360.learningservice.domain.AssignmentStatus;
import com.stackwork360.learningservice.domain.ManagerLearningAssignment;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record ManagerLearningAssignmentResponse(UUID id, String tenantId, String workerId, String managerId, UUID resourceId, LocalDate dueOn, AssignmentStatus status, Instant assignedAt, Instant updatedAt) {
    static ManagerLearningAssignmentResponse from(ManagerLearningAssignment assignment) {
        return new ManagerLearningAssignmentResponse(assignment.id(), assignment.tenantId(), assignment.workerId(), assignment.managerId(), assignment.resourceId(), assignment.dueOn(), assignment.status(), assignment.assignedAt(), assignment.updatedAt());
    }
}
