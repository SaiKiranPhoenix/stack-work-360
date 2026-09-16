package com.stackwork360.learningservice.api;

import com.stackwork360.learningservice.domain.CourseCompletion;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record CourseCompletionResponse(UUID id, String tenantId, String workerId, UUID resourceId, BigDecimal score, boolean passed, Instant completedAt) {
    static CourseCompletionResponse from(CourseCompletion completion) {
        return new CourseCompletionResponse(completion.id(), completion.tenantId(), completion.workerId(), completion.resourceId(), completion.score(), completion.passed(), completion.completedAt());
    }
}
