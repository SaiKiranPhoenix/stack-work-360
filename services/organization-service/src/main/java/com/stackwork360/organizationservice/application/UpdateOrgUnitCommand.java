package com.stackwork360.organizationservice.application;

import java.util.UUID;

public record UpdateOrgUnitCommand(
        String name,
        UUID parentId,
        String leaderWorkerId,
        String locationCode,
        String costCenterCode
) {
}
