package com.stackwork360.performanceservice.api;

import com.stackwork360.performanceservice.domain.ReviewTemplate;
import com.stackwork360.performanceservice.domain.ReviewType;
import java.util.List;
import java.util.UUID;

public record ReviewTemplateResponse(
        UUID id,
        String tenantId,
        String name,
        ReviewType type,
        List<String> competencies,
        boolean active
) {
    static ReviewTemplateResponse from(ReviewTemplate template) {
        return new ReviewTemplateResponse(template.id(), template.tenantId(), template.name(), template.type(), template.competencies(), template.active());
    }
}
