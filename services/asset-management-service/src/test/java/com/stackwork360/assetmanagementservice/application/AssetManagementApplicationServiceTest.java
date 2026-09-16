package com.stackwork360.assetmanagementservice.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.stackwork360.assetmanagementservice.domain.Asset;
import com.stackwork360.assetmanagementservice.domain.AssetLifecycleAction;
import com.stackwork360.assetmanagementservice.domain.AssetStatus;
import com.stackwork360.assetmanagementservice.domain.AssetType;
import com.stackwork360.assetmanagementservice.infrastructure.InMemoryAssetLifecycleRepository;
import com.stackwork360.assetmanagementservice.infrastructure.InMemoryAssetRepository;
import org.junit.jupiter.api.Test;

class AssetManagementApplicationServiceTest {
    private final AssetManagementApplicationService service = new AssetManagementApplicationService(
            new InMemoryAssetRepository(),
            new InMemoryAssetLifecycleRepository()
    );

    @Test
    void createsInventoryAndAssignsCoreAssetTypes() {
        create("LAP-1001", AssetType.LAPTOP);
        create("MON-1001", AssetType.MONITOR);
        create("CARD-1001", AssetType.ID_CARD);
        create("LIC-1001", AssetType.SOFTWARE_LICENSE);

        assertEquals(4, service.assets("tenant-1").size());
        assertEquals(AssetStatus.ASSIGNED, assign("LAP-1001").status());
        assertEquals(AssetStatus.ASSIGNED, assign("MON-1001").status());
        assertEquals(AssetStatus.ASSIGNED, assign("CARD-1001").status());
        assertEquals(AssetStatus.ASSIGNED, assign("LIC-1001").status());
    }

    @Test
    void returnsAssetAndKeepsLifecycleHistory() {
        create("LAP-1001", AssetType.LAPTOP);
        assign("LAP-1001");

        Asset returned = service.returnAsset(new AssetLifecycleCommand(
                "tenant-1",
                "LAP-1001",
                "it-admin",
                "Collected during exit"
        ));

        assertEquals(AssetStatus.RETURNED, returned.status());
        assertTrue(service.history("tenant-1", "LAP-1001").stream()
                .anyMatch(record -> record.action() == AssetLifecycleAction.RETURNED));
    }

    @Test
    void repairWorkflowMovesAssetBackToAvailable() {
        create("MON-1001", AssetType.MONITOR);

        service.sendForRepair(new AssetLifecycleCommand("tenant-1", "MON-1001", "it-admin", "Display flicker"));
        Asset repaired = service.markRepaired(new AssetLifecycleCommand("tenant-1", "MON-1001", "vendor-1", "Panel replaced"));

        assertEquals(AssetStatus.AVAILABLE, repaired.status());
        assertTrue(service.history("tenant-1", "MON-1001").stream()
                .anyMatch(record -> record.action() == AssetLifecycleAction.SENT_FOR_REPAIR));
        assertTrue(service.history("tenant-1", "MON-1001").stream()
                .anyMatch(record -> record.action() == AssetLifecycleAction.REPAIRED));
    }

    private Asset assign(String assetTag) {
        return service.assign(new AssignAssetCommand(
                "tenant-1",
                assetTag,
                "worker-1",
                "it-admin",
                "Issued for onboarding"
        ));
    }

    private void create(String assetTag, AssetType type) {
        service.create(new CreateAssetCommand(
                "tenant-1",
                assetTag,
                type,
                type.name() + " model",
                "SN-" + assetTag
        ));
    }
}
