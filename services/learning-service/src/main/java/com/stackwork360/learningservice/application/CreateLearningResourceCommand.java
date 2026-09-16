package com.stackwork360.learningservice.application;

import com.stackwork360.learningservice.domain.LearningResourceType;
import java.util.List;

public record CreateLearningResourceCommand(String tenantId, String title, LearningResourceType type, String provider, int durationMinutes, List<String> skills, boolean active) {
}
