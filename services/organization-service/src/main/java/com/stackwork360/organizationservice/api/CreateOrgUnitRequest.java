package com.stackwork360.organizationservice.api;

import com.stackwork360.organizationservice.domain.OrgUnitType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreateOrgUnitRequest(
        @NotBlank
        String name,

        @NotNull
        OrgUnitType type,

        UUID parentId,

        String leaderWorkerId,

        String locationCode,

        String costCenterCode
) {
}
