package com.stackwork360.skillsgraphservice.api;

import com.stackwork360.skillsgraphservice.domain.ProjectSkillMapping;
import java.util.List;
import java.util.UUID;

public record ProjectSkillMappingResponse(UUID id, String tenantId, String projectCode, List<String> skillCodes) {
    static ProjectSkillMappingResponse from(ProjectSkillMapping mapping) {
        return new ProjectSkillMappingResponse(mapping.id(), mapping.tenantId(), mapping.projectCode(), mapping.skillCodes());
    }
}
