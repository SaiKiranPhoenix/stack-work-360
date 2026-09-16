package com.stackwork360.learningservice.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.Test;

class LearningResourceTest {
    @Test
    void matchesTargetSkillsForRecommendation() {
        LearningResource resource = new LearningResource(null, "tenant-1", "Kafka Basics", LearningResourceType.COURSE, "internal", 90, List.of("Kafka", "Messaging"), true);

        assertThat(resource.teachesAny(List.of("kafka"))).isTrue();
    }

    @Test
    void requiresAtLeastOneSkill() {
        assertThatThrownBy(() -> new LearningResource(null, "tenant-1", "Empty", LearningResourceType.ARTICLE, "internal", 10, List.of(), true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("skill");
    }
}
