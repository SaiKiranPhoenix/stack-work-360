package com.stackwork360.compensationservice.infrastructure;

import com.stackwork360.compensationservice.domain.CompensationHistory;
import com.stackwork360.compensationservice.domain.CompensationHistoryRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryCompensationHistoryRepository implements CompensationHistoryRepository {
    private final Map<UUID, CompensationHistory> history = new ConcurrentHashMap<>();

    public CompensationHistory save(CompensationHistory record) {
        history.put(record.id(), record);
        return record;
    }

    public Optional<CompensationHistory> latestForWorker(String tenantId, String workerId) {
        return findByWorker(tenantId, workerId).stream().findFirst();
    }

    public List<CompensationHistory> findByTenantId(String tenantId) {
        return history.values().stream()
                .filter(record -> record.tenantId().equals(tenantId))
                .sorted(Comparator.comparing(CompensationHistory::effectiveDate).reversed())
                .toList();
    }

    public List<CompensationHistory> findByWorker(String tenantId, String workerId) {
        return history.values().stream()
                .filter(record -> record.tenantId().equals(tenantId))
                .filter(record -> record.workerId().equals(workerId))
                .sorted(Comparator.comparing(CompensationHistory::effectiveDate).reversed())
                .toList();
    }
}
