package com.stackwork360.assetmanagementservice.infrastructure;

import com.stackwork360.assetmanagementservice.domain.AssetLifecycleRecord;
import com.stackwork360.assetmanagementservice.domain.AssetLifecycleRepository;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryAssetLifecycleRepository implements AssetLifecycleRepository {
    private final ConcurrentMap<UUID, AssetLifecycleRecord> recordsById = new ConcurrentHashMap<>();

    @Override
    public AssetLifecycleRecord save(AssetLifecycleRecord record) {
        recordsById.put(record.id(), record);
        return record;
    }

    @Override
    public List<AssetLifecycleRecord> findByAssetTag(String tenantId, String assetTag) {
        return recordsById.values().stream()
                .filter(record -> record.tenantId().equals(tenantId))
                .filter(record -> record.assetTag().equals(assetTag))
                .sorted(Comparator.comparing(AssetLifecycleRecord::occurredAt).reversed())
                .toList();
    }
}
