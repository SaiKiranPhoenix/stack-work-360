package com.stackwork360.skillsgraphservice.api;

import com.stackwork360.skillsgraphservice.domain.EvidenceType;
import com.stackwork360.skillsgraphservice.domain.ProficiencyLevel;
import com.stackwork360.skillsgraphservice.domain.SkillEvidence;
import java.time.Instant;
import java.util.UUID;

public record SkillEvidenceResponse(UUID id, String tenantId, String workerId, String skillCode, EvidenceType type, String sourceId, ProficiencyLevel level, Instant capturedAt) {
    static SkillEvidenceResponse from(SkillEvidence evidence) {
        return new SkillEvidenceResponse(evidence.id(), evidence.tenantId(), evidence.workerId(), evidence.skillCode(), evidence.type(), evidence.sourceId(), evidence.level(), evidence.capturedAt());
    }
}
