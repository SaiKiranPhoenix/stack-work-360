package com.stackwork360.performanceservice.infrastructure;

import com.stackwork360.performanceservice.domain.ReviewCycle;
import com.stackwork360.performanceservice.domain.ReviewCycleRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryReviewCycleRepository implements ReviewCycleRepository {
    private final Map<UUID, ReviewCycle> cycles = new ConcurrentHashMap<>();

    public ReviewCycle save(ReviewCycle cycle) {
        cycles.put(cycle.id(), cycle);
        return cycle;
    }

    public Optional<ReviewCycle> findById(UUID id) {
        return Optional.ofNullable(cycles.get(id));
    }

    public List<ReviewCycle> findByTenantId(String tenantId) {
        return cycles.values().stream()
                .filter(cycle -> cycle.tenantId().equals(tenantId))
                .sorted(Comparator.comparing(ReviewCycle::startsOn).reversed())
                .toList();
    }
}
