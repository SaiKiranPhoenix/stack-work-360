package com.stackwork360.shiftschedulingservice.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShiftTemplateRepository {
    ShiftTemplate save(ShiftTemplate template);

    Optional<ShiftTemplate> findById(UUID id);

    List<ShiftTemplate> findByTenantId(String tenantId);
}
