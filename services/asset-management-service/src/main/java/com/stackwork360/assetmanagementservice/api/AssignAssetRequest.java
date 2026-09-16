package com.stackwork360.assetmanagementservice.api;

import jakarta.validation.constraints.NotBlank;

public record AssignAssetRequest(
        @NotBlank String workerId,
        @NotBlank String actorId,
        @NotBlank String note
) {
}
