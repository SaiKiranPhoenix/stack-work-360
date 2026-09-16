package com.stackwork360.skillsgraphservice.api;

import com.stackwork360.skillsgraphservice.domain.CertificationSkillMapping;
import java.util.List;
import java.util.UUID;

public record CertificationSkillMappingResponse(UUID id, String tenantId, String certificationCode, List<String> skillCodes) {
    static CertificationSkillMappingResponse from(CertificationSkillMapping mapping) {
        return new CertificationSkillMappingResponse(mapping.id(), mapping.tenantId(), mapping.certificationCode(), mapping.skillCodes());
    }
}
