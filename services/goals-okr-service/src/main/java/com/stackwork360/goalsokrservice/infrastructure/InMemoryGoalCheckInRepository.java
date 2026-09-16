package com.stackwork360.goalsokrservice.infrastructure;

import com.stackwork360.goalsokrservice.domain.GoalCheckIn;
import com.stackwork360.goalsokrservice.domain.GoalCheckInRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryGoalCheckInRepository implements GoalCheckInRepository {
    private final Map<UUID, GoalCheckIn> checkIns = new ConcurrentHashMap<>();

    public GoalCheckIn save(GoalCheckIn checkIn) {
        checkIns.put(checkIn.id(), checkIn);
        return checkIn;
    }

    public List<GoalCheckIn> findByGoal(String tenantId, UUID goalId) {
        return checkIns.values().stream()
                .filter(checkIn -> checkIn.tenantId().equals(tenantId))
                .filter(checkIn -> checkIn.goalId().equals(goalId))
                .sorted(Comparator.comparing(GoalCheckIn::checkedInAt).reversed())
                .toList();
    }
}
