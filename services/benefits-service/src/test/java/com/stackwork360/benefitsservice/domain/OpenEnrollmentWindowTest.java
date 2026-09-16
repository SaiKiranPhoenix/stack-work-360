package com.stackwork360.benefitsservice.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class OpenEnrollmentWindowTest {
    @Test
    void detectsActiveOpenEnrollmentDate() {
        OpenEnrollmentWindow window = new OpenEnrollmentWindow(null, "tenant-1", "FY27", LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 31));

        assertThat(window.activeOn(LocalDate.of(2026, 10, 16))).isTrue();
        assertThat(window.activeOn(LocalDate.of(2026, 11, 1))).isFalse();
    }

    @Test
    void rejectsInvalidDateRange() {
        assertThatThrownBy(() -> new OpenEnrollmentWindow(null, "tenant-1", "FY27", LocalDate.of(2026, 11, 1), LocalDate.of(2026, 10, 31)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("end date");
    }
}
