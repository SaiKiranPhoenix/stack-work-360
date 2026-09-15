package com.stackwork360.leaveservice.domain;

import java.util.List;
import java.util.Optional;

public interface LeavePolicyRepository {
    LeavePolicy save(LeavePolicy policy);

    Optional<LeavePolicy> find(String tenantId, LeaveType leaveType);

    List<LeavePolicy> findByTenantId(String tenantId);
}
