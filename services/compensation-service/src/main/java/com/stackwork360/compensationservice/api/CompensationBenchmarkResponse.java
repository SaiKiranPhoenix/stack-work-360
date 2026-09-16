package com.stackwork360.compensationservice.api;

import com.stackwork360.compensationservice.domain.CompensationBenchmark;
import java.time.Instant;
import java.util.UUID;

public record CompensationBenchmarkResponse(
        UUID id,
        String tenantId,
        String jobLevel,
        String location,
        MoneyResponse marketMedian,
        String source,
        Instant importedAt
) {
    static CompensationBenchmarkResponse from(CompensationBenchmark benchmark) {
        return new CompensationBenchmarkResponse(
                benchmark.id(),
                benchmark.tenantId(),
                benchmark.jobLevel(),
                benchmark.location(),
                MoneyResponse.from(benchmark.marketMedian()),
                benchmark.source(),
                benchmark.importedAt()
        );
    }
}
