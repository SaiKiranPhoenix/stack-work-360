package com.stackwork360.skillsgraphservice.api;

import com.stackwork360.skillsgraphservice.domain.WorkerSkillProfile;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record WorkerSkillProfileResponse(UUID id, String tenantId, String workerId, List<WorkerSkillResponse> skills, Instant updatedAt) {
    static WorkerSkillProfileResponse from(WorkerSkillProfile profile) {
        return new WorkerSkillProfileResponse(profile.id(), profile.tenantId(), profile.workerId(), profile.skills().stream().map(WorkerSkillResponse::from).toList(), profile.updatedAt());
    }
}
