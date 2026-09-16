package com.stackwork360.assetmanagementservice.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class AssetTest {
    @Test
    void assignsAndReturnsAsset() {
        Asset asset = laptop();

        asset.assignTo("worker-1");
        asset.returnAsset();

        assertEquals(AssetStatus.RETURNED, asset.status());
        assertNull(asset.assignedTo());
    }

    @Test
    void assignedAssetMustBeReturnedBeforeRepair() {
        Asset asset = laptop();
        asset.assignTo("worker-1");

        assertThrows(IllegalStateException.class, asset::sendForRepair);
    }

    @Test
    void repairsReturnedAssetBackToAvailable() {
        Asset asset = laptop();

        asset.sendForRepair();
        asset.markRepaired();

        assertEquals(AssetStatus.AVAILABLE, asset.status());
    }

    private static Asset laptop() {
        return new Asset(
                null,
                "tenant-1",
                "LAP-1001",
                AssetType.LAPTOP,
                "ThinkPad X1",
                "SN-1001",
                AssetStatus.AVAILABLE,
                null,
                null
        );
    }
}
