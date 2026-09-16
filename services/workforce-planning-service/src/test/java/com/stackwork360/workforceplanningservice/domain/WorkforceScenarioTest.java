package com.stackwork360.workforceplanningservice.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.Test;

class WorkforceScenarioTest {
    @Test
    void requiresAtLeastOneAdjustment() {
        assertThatThrownBy(() -> new WorkforceScenario(null, "tenant-1", "Empty", ScenarioType.RESTRUCTURING, List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("adjustment");
    }
}
