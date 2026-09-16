package com.stackwork360.assetmanagementservice.application;

public record AssignAssetCommand(
        String tenantId,
        String assetTag,
        String workerId,
        String actorId,
        String note
) {
}
