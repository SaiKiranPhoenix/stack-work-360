package com.stackwork360.assetmanagementservice.application;

public record AssetLifecycleCommand(
        String tenantId,
        String assetTag,
        String actorId,
        String note
) {
}
