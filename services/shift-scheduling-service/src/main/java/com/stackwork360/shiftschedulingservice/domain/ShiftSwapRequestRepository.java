package com.stackwork360.shiftschedulingservice.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShiftSwapRequestRepository {
    ShiftSwapRequest save(ShiftSwapRequest request);

    Optional<ShiftSwapRequest> findById(UUID id);

    List<ShiftSwapRequest> findByTenantId(String tenantId);
}
