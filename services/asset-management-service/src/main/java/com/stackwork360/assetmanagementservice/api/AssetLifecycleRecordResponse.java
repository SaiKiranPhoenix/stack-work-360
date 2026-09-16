package com.stackwork360.assetmanagementservice.api;

import com.stackwork360.assetmanagementservice.domain.AssetLifecycleAction;
import com.stackwork360.assetmanagementservice.domain.AssetLifecycleRecord;
import java.time.Instant;
import java.util.UUID;

public record AssetLifecycleRecordResponse(
        UUID id,
        String assetTag,
        AssetLifecycleAction action,
        String actorId,
        String note,
        Instant occurredAt
) {
    public static AssetLifecycleRecordResponse from(AssetLifecycleRecord record) {
        return new AssetLifecycleRecordResponse(
                record.id(),
                record.assetTag(),
                record.action(),
                record.actorId(),
                record.note(),
                record.occurredAt()
        );
    }
}
