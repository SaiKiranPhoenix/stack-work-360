package com.stackwork360.skillsgraphservice.domain;

import java.util.List;

public interface SkillEvidenceRepository {
    SkillEvidence save(SkillEvidence evidence);
    List<SkillEvidence> findByWorker(String tenantId, String workerId);
}
