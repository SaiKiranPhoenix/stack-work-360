package com.stackwork360.leaveservice.domain;

import java.util.List;
import java.util.Optional;

public interface LeaveBalanceRepository {
    LeaveBalance save(LeaveBalance balance);

    Optional<LeaveBalance> find(String tenantId, String workerId, LeaveType leaveType);

    List<LeaveBalance> findByTenantIdAndWorkerId(String tenantId, String workerId);
}
