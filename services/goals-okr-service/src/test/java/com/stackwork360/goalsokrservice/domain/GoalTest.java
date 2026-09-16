package com.stackwork360.goalsokrservice.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class GoalTest {
    @Test
    void updatesProgressFromKeyResultCheckIn() {
        Goal goal = goal();
        KeyResult keyResult = goal.objectives().get(0).keyResults().get(0);

        goal.updateKeyResult(keyResult.id(), new BigDecimal("50"));

        assertThat(goal.progress().value()).isEqualByComparingTo("50.00");
    }

    @Test
    void rejectsGoalWithoutObjectives() {
        assertThatThrownBy(() -> new Goal(null, "tenant-1", "worker-1", "team-1", GoalLevel.TEAM, "Empty", null, LocalDate.now(), LocalDate.now().plusDays(30), GoalStatus.DRAFT, List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("objective");
    }

    private Goal goal() {
        KeyResult keyResult = new KeyResult(null, "Ship onboarding", KeyResultType.PERCENT, BigDecimal.ZERO, new BigDecimal("100"), BigDecimal.ZERO);
        Objective objective = new Objective(null, "Improve activation", List.of(keyResult));
        return new Goal(null, "tenant-1", "worker-1", "team-1", GoalLevel.TEAM, "Grow activation", null, LocalDate.now(), LocalDate.now().plusDays(30), GoalStatus.DRAFT, List.of(objective));
    }
}
