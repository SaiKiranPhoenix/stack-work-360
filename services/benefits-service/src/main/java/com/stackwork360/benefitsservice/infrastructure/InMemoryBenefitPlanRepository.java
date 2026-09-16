package com.stackwork360.benefitsservice.infrastructure;

import com.stackwork360.benefitsservice.domain.BenefitPlan;
import com.stackwork360.benefitsservice.domain.BenefitPlanRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryBenefitPlanRepository implements BenefitPlanRepository {
    private final Map<String, BenefitPlan> plans = new ConcurrentHashMap<>();

    public BenefitPlan save(BenefitPlan plan) {
        plans.put(key(plan.tenantId(), plan.planCode()), plan);
        return plan;
    }

    public Optional<BenefitPlan> findByCode(String tenantId, String planCode) {
        return Optional.ofNullable(plans.get(key(tenantId, planCode)));
    }

    public List<BenefitPlan> findByTenantId(String tenantId) {
        return plans.values().stream()
                .filter(plan -> plan.tenantId().equals(tenantId))
                .sorted(Comparator.comparing(BenefitPlan::planCode))
                .toList();
    }

    private String key(String tenantId, String planCode) {
        return tenantId + ":" + planCode;
    }
}
