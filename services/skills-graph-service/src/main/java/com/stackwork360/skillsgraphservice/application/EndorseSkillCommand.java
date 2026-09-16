package com.stackwork360.skillsgraphservice.application;

import com.stackwork360.skillsgraphservice.domain.ProficiencyLevel;

public record EndorseSkillCommand(String tenantId, String workerId, String skillCode, String endorsedBy, ProficiencyLevel level, String note) {
}
