package com.stackwork360.shiftschedulingservice.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShiftAssignmentRepository {
    ShiftAssignment save(ShiftAssignment assignment);

    Optional<ShiftAssignment> findById(UUID id);

    List<ShiftAssignment> findByTenantIdAndDate(String tenantId, LocalDate date);

    List<ShiftAssignment> findByTenantIdAndWorkerId(String tenantId, String workerId);
}
