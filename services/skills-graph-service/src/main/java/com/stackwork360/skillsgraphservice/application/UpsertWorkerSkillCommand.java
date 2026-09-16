package com.stackwork360.skillsgraphservice.application;

import com.stackwork360.skillsgraphservice.domain.ProficiencyLevel;

public record UpsertWorkerSkillCommand(String tenantId, String workerId, String skillCode, ProficiencyLevel level) {
}
