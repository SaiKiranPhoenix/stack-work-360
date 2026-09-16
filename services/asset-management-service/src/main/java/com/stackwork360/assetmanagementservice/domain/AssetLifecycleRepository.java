package com.stackwork360.assetmanagementservice.domain;

import java.util.List;

public interface AssetLifecycleRepository {
    AssetLifecycleRecord save(AssetLifecycleRecord record);
    List<AssetLifecycleRecord> findByAssetTag(String tenantId, String assetTag);
}
