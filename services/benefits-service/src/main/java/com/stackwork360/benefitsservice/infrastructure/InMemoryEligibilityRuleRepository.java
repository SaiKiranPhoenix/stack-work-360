package com.stackwork360.benefitsservice.infrastructure;

import com.stackwork360.benefitsservice.domain.EligibilityRule;
import com.stackwork360.benefitsservice.domain.EligibilityRuleRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryEligibilityRuleRepository implements EligibilityRuleRepository {
    private final Map<UUID, EligibilityRule> rules = new ConcurrentHashMap<>();

    public EligibilityRule save(EligibilityRule rule) {
        rules.put(rule.id(), rule);
        return rule;
    }

    public List<EligibilityRule> findByPlanCode(String tenantId, String planCode) {
        return rules.values().stream()
                .filter(rule -> rule.tenantId().equals(tenantId))
                .filter(rule -> rule.planCode().equals(planCode))
                .sorted(Comparator.comparing(EligibilityRule::employmentType).thenComparing(EligibilityRule::region))
                .toList();
    }

    public List<EligibilityRule> findByTenantId(String tenantId) {
        return rules.values().stream()
                .filter(rule -> rule.tenantId().equals(tenantId))
                .sorted(Comparator.comparing(EligibilityRule::planCode))
                .toList();
    }
}
