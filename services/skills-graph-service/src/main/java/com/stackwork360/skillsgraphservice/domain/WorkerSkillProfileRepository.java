package com.stackwork360.skillsgraphservice.domain;

import java.util.List;
import java.util.Optional;

public interface WorkerSkillProfileRepository {
    WorkerSkillProfile save(WorkerSkillProfile profile);
    Optional<WorkerSkillProfile> findByWorker(String tenantId, String workerId);
    List<WorkerSkillProfile> findByTenantId(String tenantId);
}
