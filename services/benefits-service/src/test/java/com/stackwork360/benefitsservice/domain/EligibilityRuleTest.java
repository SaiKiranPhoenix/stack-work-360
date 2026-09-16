package com.stackwork360.benefitsservice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class EligibilityRuleTest {
    @Test
    void matchesEmploymentRegionAndTenure() {
        EligibilityRule rule = new EligibilityRule(null, "tenant-1", "MEDICAL", "FULL_TIME", "IN", 30);
        EmployeeProfile profile = new EmployeeProfile("worker-1", "FULL_TIME", "IN", LocalDate.now().minusDays(45));

        assertThat(rule.eligible(profile, LocalDate.now())).isTrue();
    }

    @Test
    void rejectsShortTenure() {
        EligibilityRule rule = new EligibilityRule(null, "tenant-1", "MEDICAL", "FULL_TIME", "IN", 90);
        EmployeeProfile profile = new EmployeeProfile("worker-1", "FULL_TIME", "IN", LocalDate.now().minusDays(45));

        assertThat(rule.eligible(profile, LocalDate.now())).isFalse();
    }
}
