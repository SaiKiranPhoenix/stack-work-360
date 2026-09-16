package com.stackwork360.learningservice.domain;

import java.util.List;
import java.util.UUID;

public interface CourseCompletionRepository {
    CourseCompletion save(CourseCompletion completion);
    List<CourseCompletion> findByWorker(String tenantId, String workerId);
    boolean existsForWorkerAndResource(String tenantId, String workerId, UUID resourceId);
}
