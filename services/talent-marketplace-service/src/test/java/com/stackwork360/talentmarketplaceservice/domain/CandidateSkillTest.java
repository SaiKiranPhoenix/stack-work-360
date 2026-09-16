package com.stackwork360.talentmarketplaceservice.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class CandidateSkillTest {
    @Test
    void satisfiesRequiredSkillAtSameOrHigherLevel() {
        CandidateSkill skill = new CandidateSkill("JAVA", 4);

        assertThat(skill.satisfies(new RequiredSkill("JAVA", 3))).isTrue();
        assertThat(skill.satisfies(new RequiredSkill("JAVA", 5))).isFalse();
    }

    @Test
    void rejectsInvalidLevel() {
        assertThatThrownBy(() -> new CandidateSkill("JAVA", 6))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("level");
    }
}
