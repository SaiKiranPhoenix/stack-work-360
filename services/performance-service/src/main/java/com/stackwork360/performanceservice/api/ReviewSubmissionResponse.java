package com.stackwork360.performanceservice.api;

import com.stackwork360.performanceservice.domain.ReviewStatus;
import com.stackwork360.performanceservice.domain.ReviewSubmission;
import com.stackwork360.performanceservice.domain.ReviewType;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record ReviewSubmissionResponse(
        UUID id,
        String tenantId,
        UUID cycleId,
        String subjectWorkerId,
        String reviewerWorkerId,
        ReviewType type,
        int rating,
        Integer calibratedRating,
        int finalRating,
        String narrative,
        Map<String, Integer> competencyRatings,
        ReviewStatus status,
        String calibrationNotes,
        Instant submittedAt,
        Instant updatedAt
) {
    static ReviewSubmissionResponse from(ReviewSubmission review) {
        return new ReviewSubmissionResponse(
                review.id(),
                review.tenantId(),
                review.cycleId(),
                review.subjectWorkerId(),
                review.reviewerWorkerId(),
                review.type(),
                review.rating(),
                review.calibratedRating(),
                review.finalRating(),
                review.narrative(),
                review.competencyRatings(),
                review.status(),
                review.calibrationNotes(),
                review.submittedAt(),
                review.updatedAt()
        );
    }
}
