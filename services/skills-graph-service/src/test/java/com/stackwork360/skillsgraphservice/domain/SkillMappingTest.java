package com.stackwork360.skillsgraphservice.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.Test;

class SkillMappingTest {
    @Test
    void certificationMappingRequiresSkills() {
        assertThatThrownBy(() -> new CertificationSkillMapping(null, "tenant-1", "CERT-JAVA", List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("skill");
    }

    @Test
    void projectMappingRequiresSkills() {
        assertThatThrownBy(() -> new ProjectSkillMapping(null, "tenant-1", "PAYROLL", List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("skill");
    }
}
