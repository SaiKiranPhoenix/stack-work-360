package com.stackwork360.assetmanagementservice.infrastructure;

import com.stackwork360.assetmanagementservice.domain.Asset;
import com.stackwork360.assetmanagementservice.domain.AssetRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryAssetRepository implements AssetRepository {
    private final ConcurrentMap<String, Asset> assetsByTenantAndTag = new ConcurrentHashMap<>();

    @Override
    public Asset save(Asset asset) {
        assetsByTenantAndTag.put(key(asset.tenantId(), asset.assetTag()), asset);
        return asset;
    }

    @Override
    public Optional<Asset> findByTag(String tenantId, String assetTag) {
        return Optional.ofNullable(assetsByTenantAndTag.get(key(tenantId, assetTag)));
    }

    @Override
    public List<Asset> findByTenantId(String tenantId) {
        return assetsByTenantAndTag.values().stream()
                .filter(asset -> asset.tenantId().equals(tenantId))
                .sorted(Comparator.comparing(Asset::assetTag))
                .toList();
    }

    private static String key(String tenantId, String assetTag) {
        return tenantId + ":" + assetTag;
    }
}
