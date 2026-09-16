package com.stackwork360.assetmanagementservice.domain;

import java.util.List;
import java.util.Optional;

public interface AssetRepository {
    Asset save(Asset asset);
    Optional<Asset> findByTag(String tenantId, String assetTag);
    List<Asset> findByTenantId(String tenantId);
}
