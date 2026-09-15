package com.stackwork360.organizationservice.api;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record UpdateOrgUnitRequest(
        @NotBlank
        String name,

        UUID parentId,

        String leaderWorkerId,

        String locationCode,

        String costCenterCode
) {
}
