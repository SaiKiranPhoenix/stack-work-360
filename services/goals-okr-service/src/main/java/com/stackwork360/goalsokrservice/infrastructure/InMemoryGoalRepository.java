package com.stackwork360.goalsokrservice.infrastructure;

import com.stackwork360.goalsokrservice.domain.Goal;
import com.stackwork360.goalsokrservice.domain.GoalRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryGoalRepository implements GoalRepository {
    private final Map<UUID, Goal> goals = new ConcurrentHashMap<>();

    public Goal save(Goal goal) {
        goals.put(goal.id(), goal);
        return goal;
    }

    public Optional<Goal> findById(UUID id) {
        return Optional.ofNullable(goals.get(id));
    }

    public List<Goal> findByTenantId(String tenantId) {
        return goals.values().stream()
                .filter(goal -> goal.tenantId().equals(tenantId))
                .sorted(Comparator.comparing(Goal::dueOn))
                .toList();
    }

    public List<Goal> findByTeam(String tenantId, String teamId) {
        return goals.values().stream()
                .filter(goal -> goal.tenantId().equals(tenantId))
                .filter(goal -> goal.teamId().equals(teamId))
                .sorted(Comparator.comparing(Goal::dueOn))
                .toList();
    }
}
