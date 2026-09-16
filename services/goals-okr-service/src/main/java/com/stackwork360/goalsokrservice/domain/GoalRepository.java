package com.stackwork360.goalsokrservice.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GoalRepository {
    Goal save(Goal goal);
    Optional<Goal> findById(UUID id);
    List<Goal> findByTenantId(String tenantId);
    List<Goal> findByTeam(String tenantId, String teamId);
}
