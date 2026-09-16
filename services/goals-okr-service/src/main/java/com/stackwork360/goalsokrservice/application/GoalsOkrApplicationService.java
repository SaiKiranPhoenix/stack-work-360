package com.stackwork360.goalsokrservice.application;

import com.stackwork360.goalsokrservice.domain.*;
import com.stackwork360.web.ResourceNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class GoalsOkrApplicationService {
    private final GoalRepository goalRepository;
    private final GoalCheckInRepository checkInRepository;

    public GoalsOkrApplicationService(GoalRepository goalRepository, GoalCheckInRepository checkInRepository) {
        this.goalRepository = goalRepository;
        this.checkInRepository = checkInRepository;
    }

    public Goal createGoal(CreateGoalCommand command) {
        return goalRepository.save(new Goal(null, command.tenantId(), command.ownerId(), command.teamId(), command.level(), command.title(), command.parentGoalId(), command.startsOn(), command.dueOn(), GoalStatus.DRAFT, command.objectives()));
    }

    public Goal activate(UUID goalId) {
        Goal goal = goal(goalId);
        goal.activate();
        return goalRepository.save(goal);
    }

    public GoalCheckIn checkIn(CheckInGoalCommand command) {
        Goal goal = goal(command.goalId());
        goal.updateKeyResult(command.keyResultId(), command.value());
        goalRepository.save(goal);
        return checkInRepository.save(new GoalCheckIn(null, command.tenantId(), command.goalId(), command.keyResultId(), command.value(), command.note(), command.checkedInBy(), null));
    }

    public TeamGoalRollup teamRollup(String tenantId, String teamId) {
        List<Goal> goals = goalRepository.findByTeam(tenantId, teamId);
        int completed = (int) goals.stream().filter(goal -> goal.status() == GoalStatus.COMPLETED).count();
        int atRisk = (int) goals.stream().filter(goal -> goal.status() == GoalStatus.AT_RISK).count();
        BigDecimal average = goals.isEmpty()
                ? BigDecimal.ZERO.setScale(2)
                : goals.stream()
                        .map(goal -> goal.progress().value())
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .divide(BigDecimal.valueOf(goals.size()), 2, RoundingMode.HALF_UP);
        return new TeamGoalRollup(tenantId, teamId, goals.size(), completed, atRisk, average);
    }

    public GoalAlignmentView alignment(String tenantId) {
        List<GoalAlignmentNode> nodes = goalRepository.findByTenantId(tenantId).stream()
                .sorted(Comparator.comparing(Goal::level).thenComparing(Goal::title))
                .map(GoalAlignmentNode::from)
                .toList();
        return new GoalAlignmentView(tenantId, nodes);
    }

    public List<Goal> goals(String tenantId) {
        return goalRepository.findByTenantId(tenantId);
    }

    public List<GoalCheckIn> checkIns(String tenantId, UUID goalId) {
        return checkInRepository.findByGoal(tenantId, goalId);
    }

    private Goal goal(UUID id) {
        return goalRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("goal not found"));
    }
}
