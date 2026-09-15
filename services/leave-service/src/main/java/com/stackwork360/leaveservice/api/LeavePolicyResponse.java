package com.stackwork360.leaveservice.api;

import com.stackwork360.leaveservice.domain.LeavePolicy;
import com.stackwork360.leaveservice.domain.LeaveType;
import java.math.BigDecimal;

public record LeavePolicyResponse(
        LeaveType leaveType,
        BigDecimal annualAllowanceDays,
        boolean requiresApproval,
        boolean paid
) {
    public static LeavePolicyResponse from(LeavePolicy policy) {
        return new LeavePolicyResponse(
                policy.leaveType(),
                policy.annualAllowanceDays(),
                policy.requiresApproval(),
                policy.paid()
        );
    }
}
