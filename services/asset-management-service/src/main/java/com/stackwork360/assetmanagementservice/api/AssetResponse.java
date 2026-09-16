package com.stackwork360.assetmanagementservice.api;

import com.stackwork360.assetmanagementservice.domain.Asset;
import com.stackwork360.assetmanagementservice.domain.AssetStatus;
import com.stackwork360.assetmanagementservice.domain.AssetType;
import java.time.Instant;
import java.util.UUID;

public record AssetResponse(
        UUID id,
        String assetTag,
        AssetType type,
        String model,
        String serialNumber,
        AssetStatus status,
        String assignedTo,
        Instant updatedAt
) {
    public static AssetResponse from(Asset asset) {
        return new AssetResponse(
                asset.id(),
                asset.assetTag(),
                asset.type(),
                asset.model(),
                asset.serialNumber(),
                asset.status(),
                asset.assignedTo(),
                asset.updatedAt()
        );
    }
}
