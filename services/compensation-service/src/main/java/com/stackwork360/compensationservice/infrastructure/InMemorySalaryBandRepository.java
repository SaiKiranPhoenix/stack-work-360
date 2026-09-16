package com.stackwork360.compensationservice.infrastructure;

import com.stackwork360.compensationservice.domain.SalaryBand;
import com.stackwork360.compensationservice.domain.SalaryBandRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemorySalaryBandRepository implements SalaryBandRepository {
    private final Map<UUID, SalaryBand> bands = new ConcurrentHashMap<>();

    public SalaryBand save(SalaryBand band) {
        bands.put(band.id(), band);
        return band;
    }

    public Optional<SalaryBand> findById(UUID id) {
        return Optional.ofNullable(bands.get(id));
    }

    public Optional<SalaryBand> find(String tenantId, String jobLevel, String location) {
        return bands.values().stream()
                .filter(band -> band.tenantId().equals(tenantId))
                .filter(band -> band.jobLevel().equals(jobLevel))
                .filter(band -> band.location().equals(location))
                .findFirst();
    }

    public List<SalaryBand> findByTenantId(String tenantId) {
        return bands.values().stream()
                .filter(band -> band.tenantId().equals(tenantId))
                .sorted(Comparator.comparing(SalaryBand::jobLevel).thenComparing(SalaryBand::location))
                .toList();
    }
}
