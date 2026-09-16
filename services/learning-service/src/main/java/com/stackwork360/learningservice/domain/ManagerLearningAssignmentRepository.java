package com.stackwork360.learningservice.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ManagerLearningAssignmentRepository {
    ManagerLearningAssignment save(ManagerLearningAssignment assignment);
    Optional<ManagerLearningAssignment> findById(UUID id);
    List<ManagerLearningAssignment> findByWorker(String tenantId, String workerId);
}
