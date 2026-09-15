package com.stackwork360.riskengineservice.infrastructure;

import com.stackwork360.riskengineservice.domain.RiskSignal;
import com.stackwork360.riskengineservice.domain.RiskSignalRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryRiskSignalRepository implements RiskSignalRepository {
    private final Map<UUID, RiskSignal> signals = new ConcurrentHashMap<>();

    @Override
    public RiskSignal save(RiskSignal signal) {
        signals.put(signal.id(), signal);
        return signal;
    }

    @Override
    public Optional<RiskSignal> findById(UUID id) {
        return Optional.ofNullable(signals.get(id));
    }

    @Override
    public List<RiskSignal> findByTenantId(String tenantId) {
        return signals.values().stream()
                .filter(signal -> signal.tenantId().equals(tenantId))
                .sorted(Comparator.comparing(RiskSignal::createdAt).reversed())
                .toList();
    }

    @Override
    public List<RiskSignal> findByTenantIdAndSubjectId(String tenantId, String subjectId) {
        return signals.values().stream()
                .filter(signal -> signal.tenantId().equals(tenantId))
                .filter(signal -> signal.subjectId().equals(subjectId))
                .sorted(Comparator.comparing(RiskSignal::createdAt).reversed())
                .toList();
    }
}
