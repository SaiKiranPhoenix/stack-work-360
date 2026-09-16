package com.stackwork360.skillsgraphservice.api;

import com.stackwork360.skillsgraphservice.domain.Skill;
import com.stackwork360.skillsgraphservice.domain.SkillCategory;
import java.util.UUID;

public record SkillResponse(UUID id, String tenantId, String code, String name, SkillCategory category, String parentSkillCode, boolean active) {
    static SkillResponse from(Skill skill) {
        return new SkillResponse(skill.id(), skill.tenantId(), skill.code(), skill.name(), skill.category(), skill.parentSkillCode(), skill.active());
    }
}
