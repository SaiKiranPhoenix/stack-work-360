package com.stackwork360.skillsgraphservice.api;

import com.stackwork360.skillsgraphservice.domain.ProficiencyLevel;
import com.stackwork360.skillsgraphservice.domain.WorkerSkill;
import java.time.Instant;

public record WorkerSkillResponse(String skillCode, ProficiencyLevel level, Instant updatedAt) {
    static WorkerSkillResponse from(WorkerSkill skill) {
        return new WorkerSkillResponse(skill.skillCode(), skill.level(), skill.updatedAt());
    }
}
