package com.stackwork360.assetmanagementservice.api;

import jakarta.validation.constraints.NotBlank;

public record AssetLifecycleRequest(
        @NotBlank String actorId,
        @NotBlank String note
) {
}
