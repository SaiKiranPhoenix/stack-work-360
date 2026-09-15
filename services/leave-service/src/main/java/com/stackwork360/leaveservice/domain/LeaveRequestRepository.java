package com.stackwork360.leaveservice.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LeaveRequestRepository {
    LeaveRequest save(LeaveRequest leaveRequest);

    Optional<LeaveRequest> findById(UUID id);

    List<LeaveRequest> findByTenantId(String tenantId);

    List<LeaveRequest> findByTenantIdAndWorkerId(String tenantId, String workerId);
}
