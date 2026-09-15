package com.stackwork360.riskengineservice.infrastructure;

import com.stackwork360.riskengineservice.domain.RiskRule;
import com.stackwork360.riskengineservice.domain.RiskRuleRepository;
import com.stackwork360.riskengineservice.domain.RiskSource;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryRiskRuleRepository implements RiskRuleRepository {
    private final Map<UUID, RiskRule> rules = new ConcurrentHashMap<>();

    @Override
    public RiskRule save(RiskRule rule) {
        rules.put(rule.id(), rule);
        return rule;
    }

    @Override
    public Optional<RiskRule> findById(UUID id) {
        return Optional.ofNullable(rules.get(id));
    }

    @Override
    public List<RiskRule> findByTenantId(String tenantId) {
        return rules.values().stream()
                .filter(rule -> rule.tenantId().equals(tenantId))
                .sorted(Comparator.comparing(RiskRule::name))
                .toList();
    }

    @Override
    public List<RiskRule> findByTenantIdAndSource(String tenantId, RiskSource source) {
        return rules.values().stream()
                .filter(rule -> rule.tenantId().equals(tenantId))
                .filter(rule -> rule.source() == source)
                .sorted(Comparator.comparing(RiskRule::name))
                .toList();
    }
}
