package com.stackwork360.workforceplanningservice.infrastructure;

import com.stackwork360.workforceplanningservice.domain.WorkforceScenario;
import com.stackwork360.workforceplanningservice.domain.WorkforceScenarioRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryWorkforceScenarioRepository implements WorkforceScenarioRepository {
    private final Map<UUID, WorkforceScenario> scenarios = new ConcurrentHashMap<>();

    public WorkforceScenario save(WorkforceScenario scenario) {
        scenarios.put(scenario.id(), scenario);
        return scenario;
    }

    public Optional<WorkforceScenario> findById(UUID id) {
        return Optional.ofNullable(scenarios.get(id));
    }

    public List<WorkforceScenario> findByTenantId(String tenantId) {
        return scenarios.values().stream()
                .filter(scenario -> scenario.tenantId().equals(tenantId))
                .sorted(Comparator.comparing(WorkforceScenario::name))
                .toList();
    }
}
