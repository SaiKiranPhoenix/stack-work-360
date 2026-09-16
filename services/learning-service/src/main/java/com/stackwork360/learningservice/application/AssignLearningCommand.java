package com.stackwork360.learningservice.application;

import java.time.LocalDate;
import java.util.UUID;

public record AssignLearningCommand(String tenantId, String workerId, String managerId, UUID resourceId, LocalDate dueOn) {
}
