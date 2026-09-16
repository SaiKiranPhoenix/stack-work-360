package com.stackwork360.workforceplanningservice.infrastructure;

import com.stackwork360.workforceplanningservice.domain.HeadcountPlan;
import com.stackwork360.workforceplanningservice.domain.HeadcountPlanRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryHeadcountPlanRepository implements HeadcountPlanRepository {
    private final Map<UUID, HeadcountPlan> plans = new ConcurrentHashMap<>();

    public HeadcountPlan save(HeadcountPlan plan) {
        plans.put(plan.id(), plan);
        return plan;
    }

    public Optional<HeadcountPlan> findById(UUID id) {
        return Optional.ofNullable(plans.get(id));
    }

    public List<HeadcountPlan> findByTenantId(String tenantId) {
        return plans.values().stream()
                .filter(plan -> plan.tenantId().equals(tenantId))
                .sorted(Comparator.comparing(HeadcountPlan::teamId).thenComparing(HeadcountPlan::location))
                .toList();
    }
}
