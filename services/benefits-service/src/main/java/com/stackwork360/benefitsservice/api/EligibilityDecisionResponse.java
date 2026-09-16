package com.stackwork360.benefitsservice.api;

import com.stackwork360.benefitsservice.application.EligibilityDecision;
import java.util.List;

public record EligibilityDecisionResponse(
        String tenantId,
        String workerId,
        String planCode,
        boolean eligible,
        List<String> reasons
) {
    static EligibilityDecisionResponse from(EligibilityDecision decision) {
        return new EligibilityDecisionResponse(decision.tenantId(), decision.workerId(), decision.planCode(), decision.eligible(), decision.reasons());
    }
}
