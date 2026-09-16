package com.stackwork360.performanceservice.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class ReviewCycleTest {
    @Test
    void movesThroughLifecycle() {
        ReviewCycle cycle = new ReviewCycle(null, "tenant-1", "FY27 H1", LocalDate.now(), LocalDate.now().plusDays(30), ReviewCycleStatus.DRAFT);

        cycle.activate();
        cycle.startCalibration();
        cycle.close();

        assertThat(cycle.status()).isEqualTo(ReviewCycleStatus.CLOSED);
    }

    @Test
    void rejectsInvalidDateRange() {
        assertThatThrownBy(() -> new ReviewCycle(null, "tenant-1", "FY27 H1", LocalDate.now().plusDays(5), LocalDate.now(), ReviewCycleStatus.DRAFT))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("end date");
    }
}
