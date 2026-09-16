package com.stackwork360.skillsgraphservice.domain;

import java.util.List;

public interface SkillEndorsementRepository {
    SkillEndorsement save(SkillEndorsement endorsement);
    List<SkillEndorsement> findByWorker(String tenantId, String workerId);
}
