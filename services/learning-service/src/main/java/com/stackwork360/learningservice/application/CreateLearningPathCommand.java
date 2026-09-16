package com.stackwork360.learningservice.application;

import java.util.List;
import java.util.UUID;

public record CreateLearningPathCommand(String tenantId, String title, String roleTarget, List<UUID> resourceIds, boolean active) {
}
