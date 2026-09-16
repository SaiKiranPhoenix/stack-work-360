package com.stackwork360.assetmanagementservice.application;

import com.stackwork360.assetmanagementservice.domain.AssetType;

public record CreateAssetCommand(
        String tenantId,
        String assetTag,
        AssetType type,
        String model,
        String serialNumber
) {
}
