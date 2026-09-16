package com.stackwork360.skillsgraphservice.api;

import com.stackwork360.skillsgraphservice.domain.ProficiencyLevel;
import com.stackwork360.skillsgraphservice.domain.SkillEndorsement;
import java.time.Instant;
import java.util.UUID;

public record SkillEndorsementResponse(UUID id, String tenantId, String workerId, String skillCode, String endorsedBy, ProficiencyLevel level, String note, Instant endorsedAt) {
    static SkillEndorsementResponse from(SkillEndorsement endorsement) {
        return new SkillEndorsementResponse(endorsement.id(), endorsement.tenantId(), endorsement.workerId(), endorsement.skillCode(), endorsement.endorsedBy(), endorsement.level(), endorsement.note(), endorsement.endorsedAt());
    }
}
