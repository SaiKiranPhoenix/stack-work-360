package com.stackwork360.skillsgraphservice.domain;

import java.util.List;

public interface ProjectSkillMappingRepository {
    ProjectSkillMapping save(ProjectSkillMapping mapping);
    List<ProjectSkillMapping> findByTenantId(String tenantId);
}
