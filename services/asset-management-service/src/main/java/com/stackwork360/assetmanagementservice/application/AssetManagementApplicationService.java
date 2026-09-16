package com.stackwork360.assetmanagementservice.application;

import com.stackwork360.assetmanagementservice.domain.Asset;
import com.stackwork360.assetmanagementservice.domain.AssetLifecycleAction;
import com.stackwork360.assetmanagementservice.domain.AssetLifecycleRecord;
import com.stackwork360.assetmanagementservice.domain.AssetLifecycleRepository;
import com.stackwork360.assetmanagementservice.domain.AssetRepository;
import com.stackwork360.assetmanagementservice.domain.AssetStatus;
import com.stackwork360.web.ResourceNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AssetManagementApplicationService {
    private final AssetRepository assetRepository;
    private final AssetLifecycleRepository lifecycleRepository;

    public AssetManagementApplicationService(AssetRepository assetRepository, AssetLifecycleRepository lifecycleRepository) {
        this.assetRepository = assetRepository;
        this.lifecycleRepository = lifecycleRepository;
    }

    public Asset create(CreateAssetCommand command) {
        Asset asset = new Asset(
                null,
                command.tenantId(),
                command.assetTag(),
                command.type(),
                command.model(),
                command.serialNumber(),
                AssetStatus.AVAILABLE,
                null,
                null
        );
        Asset saved = assetRepository.save(asset);
        record(command.tenantId(), command.assetTag(), AssetLifecycleAction.CREATED, "system", "Asset added to inventory");
        return saved;
    }

    public Asset assign(AssignAssetCommand command) {
        Asset asset = findAsset(command.tenantId(), command.assetTag());
        asset.assignTo(command.workerId());
        Asset saved = assetRepository.save(asset);
        record(command.tenantId(), command.assetTag(), AssetLifecycleAction.ASSIGNED, command.actorId(), command.note());
        return saved;
    }

    public Asset returnAsset(AssetLifecycleCommand command) {
        Asset asset = findAsset(command.tenantId(), command.assetTag());
        asset.returnAsset();
        Asset saved = assetRepository.save(asset);
        record(command.tenantId(), command.assetTag(), AssetLifecycleAction.RETURNED, command.actorId(), command.note());
        return saved;
    }

    public Asset sendForRepair(AssetLifecycleCommand command) {
        Asset asset = findAsset(command.tenantId(), command.assetTag());
        asset.sendForRepair();
        Asset saved = assetRepository.save(asset);
        record(command.tenantId(), command.assetTag(), AssetLifecycleAction.SENT_FOR_REPAIR, command.actorId(), command.note());
        return saved;
    }

    public Asset markRepaired(AssetLifecycleCommand command) {
        Asset asset = findAsset(command.tenantId(), command.assetTag());
        asset.markRepaired();
        Asset saved = assetRepository.save(asset);
        record(command.tenantId(), command.assetTag(), AssetLifecycleAction.REPAIRED, command.actorId(), command.note());
        return saved;
    }

    public List<Asset> assets(String tenantId) {
        return assetRepository.findByTenantId(tenantId);
    }

    public List<AssetLifecycleRecord> history(String tenantId, String assetTag) {
        findAsset(tenantId, assetTag);
        return lifecycleRepository.findByAssetTag(tenantId, assetTag);
    }

    private Asset findAsset(String tenantId, String assetTag) {
        return assetRepository.findByTag(tenantId, assetTag)
                .orElseThrow(() -> new ResourceNotFoundException("Asset %s was not found".formatted(assetTag)));
    }

    private void record(String tenantId, String assetTag, AssetLifecycleAction action, String actorId, String note) {
        lifecycleRepository.save(new AssetLifecycleRecord(null, tenantId, assetTag, action, actorId, note, null));
    }
}
