package com.stackwork360.skillsgraphservice.domain;

import java.util.List;

public interface CertificationSkillMappingRepository {
    CertificationSkillMapping save(CertificationSkillMapping mapping);
    List<CertificationSkillMapping> findByTenantId(String tenantId);
}
