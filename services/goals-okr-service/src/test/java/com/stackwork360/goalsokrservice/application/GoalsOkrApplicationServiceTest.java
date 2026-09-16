package com.stackwork360.goalsokrservice.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.stackwork360.goalsokrservice.domain.*;
import com.stackwork360.goalsokrservice.infrastructure.InMemoryGoalCheckInRepository;
import com.stackwork360.goalsokrservice.infrastructure.InMemoryGoalRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class GoalsOkrApplicationServiceTest {
    private final GoalsOkrApplicationService service = new GoalsOkrApplicationService(
            new InMemoryGoalRepository(),
            new InMemoryGoalCheckInRepository()
    );

    @Test
    void recordsCheckInAndUpdatesTeamRollup() {
        Goal goal = service.createGoal(command(null, "team-1", "Team activation"));
        service.activate(goal.id());
        KeyResult keyResult = goal.objectives().get(0).keyResults().get(0);

        service.checkIn(new CheckInGoalCommand("tenant-1", goal.id(), keyResult.id(), new BigDecimal("75"), "ahead of plan", "worker-1"));

        TeamGoalRollup rollup = service.teamRollup("tenant-1", "team-1");
        assertThat(rollup.goalCount()).isEqualTo(1);
        assertThat(rollup.averageProgress()).isEqualByComparingTo("75.00");
        assertThat(service.checkIns("tenant-1", goal.id())).hasSize(1);
    }

    @Test
    void buildsAlignmentViewWithParentRelationship() {
        Goal parent = service.createGoal(command(null, "company", "Company growth"));
        Goal child = service.createGoal(command(parent.id(), "team-1", "Team activation"));

        GoalAlignmentView view = service.alignment("tenant-1");

        assertThat(view.nodes()).hasSize(2);
        assertThat(view.nodes())
                .anySatisfy(node -> {
                    assertThat(node.goalId()).isEqualTo(child.id());
                    assertThat(node.parentGoalId()).isEqualTo(parent.id());
                });
    }

    private CreateGoalCommand command(java.util.UUID parentGoalId, String teamId, String title) {
        KeyResult keyResult = new KeyResult(null, "Progress KR", KeyResultType.PERCENT, BigDecimal.ZERO, new BigDecimal("100"), BigDecimal.ZERO);
        Objective objective = new Objective(null, "Progress objective", List.of(keyResult));
        return new CreateGoalCommand("tenant-1", "owner-1", teamId, GoalLevel.TEAM, title, parentGoalId, LocalDate.now(), LocalDate.now().plusDays(90), List.of(objective));
    }
}
