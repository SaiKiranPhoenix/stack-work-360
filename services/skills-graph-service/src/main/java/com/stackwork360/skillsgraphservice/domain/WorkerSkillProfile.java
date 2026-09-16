package com.stackwork360.skillsgraphservice.domain;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class WorkerSkillProfile {
    private final UUID id;
    private final String tenantId;
    private final String workerId;
    private final Map<String, WorkerSkill> skills;
    private Instant updatedAt;

    public WorkerSkillProfile(UUID id, String tenantId, String workerId, List<WorkerSkill> skills, Instant updatedAt) {
        this.id = id == null ? UUID.randomUUID() : id;
        this.tenantId = requireText(tenantId, "tenant id is required");
        this.workerId = requireText(workerId, "worker id is required");
        this.skills = new HashMap<>();
        Objects.requireNonNull(skills, "skills are required").forEach(skill -> this.skills.put(skill.skillCode(), skill));
        this.updatedAt = updatedAt == null ? Instant.now() : updatedAt;
    }

    public void upsertSkill(String skillCode, ProficiencyLevel level) {
        skills.put(requireText(skillCode, "skill code is required"), new WorkerSkill(skillCode, level, null));
        updatedAt = Instant.now();
    }

    public boolean hasSkill(String skillCode, ProficiencyLevel requiredLevel) {
        WorkerSkill skill = skills.get(skillCode);
        return skill != null && skill.level().atLeast(requiredLevel);
    }

    public UUID id() { return id; }
    public String tenantId() { return tenantId; }
    public String workerId() { return workerId; }
    public List<WorkerSkill> skills() { return List.copyOf(skills.values()); }
    public Instant updatedAt() { return updatedAt; }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
