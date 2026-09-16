package com.stackwork360.performanceservice.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewSubmissionRepository {
    ReviewSubmission save(ReviewSubmission submission);
    Optional<ReviewSubmission> findById(UUID id);
    List<ReviewSubmission> findByTenantId(String tenantId);
    List<ReviewSubmission> findByWorker(String tenantId, String workerId);
    List<ReviewSubmission> findByCycle(String tenantId, UUID cycleId);
}
