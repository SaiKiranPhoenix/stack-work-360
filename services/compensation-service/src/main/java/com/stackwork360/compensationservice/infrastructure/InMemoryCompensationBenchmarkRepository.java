package com.stackwork360.compensationservice.infrastructure;

import com.stackwork360.compensationservice.domain.CompensationBenchmark;
import com.stackwork360.compensationservice.domain.CompensationBenchmarkRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryCompensationBenchmarkRepository implements CompensationBenchmarkRepository {
    private final Map<UUID, CompensationBenchmark> benchmarks = new ConcurrentHashMap<>();

    public CompensationBenchmark save(CompensationBenchmark benchmark) {
        benchmarks.put(benchmark.id(), benchmark);
        return benchmark;
    }

    public Optional<CompensationBenchmark> find(String tenantId, String jobLevel, String location) {
        return benchmarks.values().stream()
                .filter(benchmark -> benchmark.tenantId().equals(tenantId))
                .filter(benchmark -> benchmark.jobLevel().equals(jobLevel))
                .filter(benchmark -> benchmark.location().equals(location))
                .findFirst();
    }

    public List<CompensationBenchmark> findByTenantId(String tenantId) {
        return benchmarks.values().stream()
                .filter(benchmark -> benchmark.tenantId().equals(tenantId))
                .sorted(Comparator.comparing(CompensationBenchmark::jobLevel).thenComparing(CompensationBenchmark::location))
                .toList();
    }
}
