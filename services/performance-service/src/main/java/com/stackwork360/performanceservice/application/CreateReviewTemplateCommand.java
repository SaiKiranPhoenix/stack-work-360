package com.stackwork360.performanceservice.application;

import com.stackwork360.performanceservice.domain.ReviewType;
import java.util.List;

public record CreateReviewTemplateCommand(String tenantId, String name, ReviewType type, List<String> competencies, boolean active) {
}
