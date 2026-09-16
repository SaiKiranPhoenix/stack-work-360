package com.stackwork360.compensationservice.domain;

import java.util.List;
import java.util.Optional;

public interface CompensationBenchmarkRepository {
    CompensationBenchmark save(CompensationBenchmark benchmark);
    Optional<CompensationBenchmark> find(String tenantId, String jobLevel, String location);
    List<CompensationBenchmark> findByTenantId(String tenantId);
}
