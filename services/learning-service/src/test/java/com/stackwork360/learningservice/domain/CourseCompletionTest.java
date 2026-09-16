package com.stackwork360.learningservice.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class CourseCompletionTest {
    @Test
    void detectsPassingCompletion() {
        CourseCompletion completion = new CourseCompletion(null, "tenant-1", "worker-1", UUID.randomUUID(), new BigDecimal("85"), null);

        assertThat(completion.passed()).isTrue();
    }

    @Test
    void rejectsInvalidScore() {
        assertThatThrownBy(() -> new CourseCompletion(null, "tenant-1", "worker-1", UUID.randomUUID(), new BigDecimal("120"), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("score");
    }
}
