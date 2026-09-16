package com.stackwork360.learningservice.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record CourseCompletion(
        UUID id,
        String tenantId,
        String workerId,
        UUID resourceId,
        BigDecimal score,
        Instant completedAt
) {
    public CourseCompletion {
        id = id == null ? UUID.randomUUID() : id;
        tenantId = requireText(tenantId, "tenant id is required");
        workerId = requireText(workerId, "worker id is required");
        resourceId = Objects.requireNonNull(resourceId, "resource id is required");
        score = Objects.requireNonNull(score, "score is required").setScale(2, RoundingMode.HALF_UP);
        if (score.compareTo(BigDecimal.ZERO) < 0 || score.compareTo(new BigDecimal("100")) > 0) {
            throw new IllegalArgumentException("completion score must be between 0 and 100");
        }
        completedAt = completedAt == null ? Instant.now() : completedAt;
    }

    public boolean passed() {
        return score.compareTo(new BigDecimal("70.00")) >= 0;
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
