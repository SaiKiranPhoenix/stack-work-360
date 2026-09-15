package com.stackwork360.organizationservice.api;

import com.stackwork360.organizationservice.domain.OrgUnit;
import com.stackwork360.organizationservice.domain.OrgUnitType;
import java.time.Instant;
import java.util.UUID;

public record OrgUnitResponse(
        UUID id,
        String tenantId,
        String name,
        OrgUnitType type,
        UUID parentId,
        String leaderWorkerId,
        String locationCode,
        String costCenterCode,
        boolean active,
        Instant createdAt,
        Instant updatedAt
) {
    public static OrgUnitResponse from(OrgUnit orgUnit) {
        return new OrgUnitResponse(
                orgUnit.id(),
                orgUnit.tenantId(),
                orgUnit.name(),
                orgUnit.type(),
                orgUnit.parentId(),
                orgUnit.leaderWorkerId(),
                orgUnit.locationCode(),
                orgUnit.costCenterCode(),
                orgUnit.active(),
                orgUnit.createdAt(),
                orgUnit.updatedAt()
        );
    }
}
