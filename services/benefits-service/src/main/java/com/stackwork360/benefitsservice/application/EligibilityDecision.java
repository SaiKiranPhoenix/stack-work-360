package com.stackwork360.benefitsservice.application;

import java.util.List;

public record EligibilityDecision(
        String tenantId,
        String workerId,
        String planCode,
        boolean eligible,
        List<String> reasons
) {
}
