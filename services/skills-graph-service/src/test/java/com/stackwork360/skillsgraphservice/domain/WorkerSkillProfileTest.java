package com.stackwork360.skillsgraphservice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class WorkerSkillProfileTest {
    @Test
    void matchesRequiredProficiencyOrHigher() {
        WorkerSkillProfile profile = new WorkerSkillProfile(null, "tenant-1", "worker-1", List.of(), null);

        profile.upsertSkill("JAVA", ProficiencyLevel.ADVANCED);

        assertThat(profile.hasSkill("JAVA", ProficiencyLevel.PROFICIENT)).isTrue();
        assertThat(profile.hasSkill("JAVA", ProficiencyLevel.EXPERT)).isFalse();
    }
}
