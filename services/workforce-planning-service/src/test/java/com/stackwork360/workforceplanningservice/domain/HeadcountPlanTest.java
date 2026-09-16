package com.stackwork360.workforceplanningservice.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class HeadcountPlanTest {
    @Test
    void calculatesTargetCostAndCapacity() {
        HeadcountPlan plan = plan();

        assertThat(plan.targetHeadcount()).isEqualTo(7);
        assertThat(plan.targetCost()).isEqualByComparingTo("780000.00");
        assertThat(TeamCapacity.from(plan).weeklyCapacityHours()).isEqualByComparingTo("224.00");
    }

    @Test
    void rejectsNegativeHeadcount() {
        assertThatThrownBy(() -> new HeadcountPlan(null, "tenant-1", "team-1", "IN", -1, 0, 1, 0, new BigDecimal("100000"), new BigDecimal("90000")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("headcount");
    }

    private HeadcountPlan plan() {
        return new HeadcountPlan(null, "tenant-1", "team-1", "IN", 3, 1, 6, 1, new BigDecimal("115000"), new BigDecimal("90000"));
    }
}
