package com.stackwork360.goalsokrservice.domain;

import java.util.List;
import java.util.UUID;

public interface GoalCheckInRepository {
    GoalCheckIn save(GoalCheckIn checkIn);
    List<GoalCheckIn> findByGoal(String tenantId, UUID goalId);
}
