package com.stackwork360.workforceplanningservice.infrastructure;

import com.stackwork360.workforceplanningservice.domain.HiringPlan;
import com.stackwork360.workforceplanningservice.domain.HiringPlanRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryHiringPlanRepository implements HiringPlanRepository {
    private final Map<UUID, HiringPlan> plans = new ConcurrentHashMap<>();

    public HiringPlan save(HiringPlan plan) {
        plans.put(plan.id(), plan);
        return plan;
    }

    public List<HiringPlan> findByTenantId(String tenantId) {
        return plans.values().stream()
                .filter(plan -> plan.tenantId().equals(tenantId))
                .sorted(Comparator.comparing(HiringPlan::targetStartDate))
                .toList();
    }
}
