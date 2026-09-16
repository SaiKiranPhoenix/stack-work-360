package com.stackwork360.skillsgraphservice.domain;

import java.math.BigDecimal;
import java.util.List;

public record SuccessionSkillCoverage(
        String tenantId,
        String roleName,
        int candidateCount,
        BigDecimal averageCoverage,
        List<String> readyWorkerIds
) {
}
