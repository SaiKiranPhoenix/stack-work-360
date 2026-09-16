package com.stackwork360.assetmanagementservice.api;

import com.stackwork360.assetmanagementservice.domain.AssetType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateAssetRequest(
        @NotBlank String assetTag,
        @NotNull AssetType type,
        @NotBlank String model,
        @NotBlank String serialNumber
) {
}
