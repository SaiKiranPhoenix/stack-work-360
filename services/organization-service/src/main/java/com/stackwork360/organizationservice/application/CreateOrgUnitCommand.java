package com.stackwork360.organizationservice.application;

import com.stackwork360.organizationservice.domain.OrgUnitType;
import java.util.UUID;

public record CreateOrgUnitCommand(
        String tenantId,
        String name,
        OrgUnitType type,
        UUID parentId,
        String leaderWorkerId,
        String locationCode,
        String costCenterCode
) {
}
