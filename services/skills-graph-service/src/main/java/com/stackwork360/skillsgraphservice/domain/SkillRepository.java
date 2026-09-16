package com.stackwork360.skillsgraphservice.domain;

import java.util.List;
import java.util.Optional;

public interface SkillRepository {
    Skill save(Skill skill);
    Optional<Skill> findByCode(String tenantId, String code);
    List<Skill> findByTenantId(String tenantId);
}
