package com.stackwork360.skillsgraphservice.application;

import com.stackwork360.skillsgraphservice.domain.SkillCategory;

public record CreateSkillCommand(String tenantId, String code, String name, SkillCategory category, String parentSkillCode, boolean active) {
}
