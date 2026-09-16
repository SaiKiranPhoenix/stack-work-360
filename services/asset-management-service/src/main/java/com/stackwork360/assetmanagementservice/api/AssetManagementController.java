package com.stackwork360.assetmanagementservice.api;

import com.stackwork360.assetmanagementservice.application.AssetLifecycleCommand;
import com.stackwork360.assetmanagementservice.application.AssetManagementApplicationService;
import com.stackwork360.assetmanagementservice.application.AssignAssetCommand;
import com.stackwork360.assetmanagementservice.application.CreateAssetCommand;
import com.stackwork360.common.CorrelationIds;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/assets/v1")
public class AssetManagementController {
    private final AssetManagementApplicationService assetManagementApplicationService;

    public AssetManagementController(AssetManagementApplicationService assetManagementApplicationService) {
        this.assetManagementApplicationService = assetManagementApplicationService;
    }

    @PostMapping("/assets")
    public ResponseEntity<AssetResponse> create(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @Valid @RequestBody CreateAssetRequest request
    ) {
        AssetResponse response = AssetResponse.from(assetManagementApplicationService.create(new CreateAssetCommand(
                tenantId,
                request.assetTag(),
                request.type(),
                request.model(),
                request.serialNumber()
        )));
        return ResponseEntity.created(URI.create("/api/assets/v1/assets/" + response.assetTag()))
                .body(response);
    }

    @GetMapping("/assets")
    public List<AssetResponse> list(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return assetManagementApplicationService.assets(tenantId).stream().map(AssetResponse::from).toList();
    }

    @PatchMapping("/assets/{assetTag}/assign")
    public AssetResponse assign(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @PathVariable String assetTag,
            @Valid @RequestBody AssignAssetRequest request
    ) {
        return AssetResponse.from(assetManagementApplicationService.assign(new AssignAssetCommand(
                tenantId,
                assetTag,
                request.workerId(),
                request.actorId(),
                request.note()
        )));
    }

    @PatchMapping("/assets/{assetTag}/return")
    public AssetResponse returnAsset(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @PathVariable String assetTag,
            @Valid @RequestBody AssetLifecycleRequest request
    ) {
        return AssetResponse.from(assetManagementApplicationService.returnAsset(command(tenantId, assetTag, request)));
    }

    @PatchMapping("/assets/{assetTag}/repair")
    public AssetResponse sendForRepair(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @PathVariable String assetTag,
            @Valid @RequestBody AssetLifecycleRequest request
    ) {
        return AssetResponse.from(assetManagementApplicationService.sendForRepair(command(tenantId, assetTag, request)));
    }

    @PatchMapping("/assets/{assetTag}/repair/complete")
    public AssetResponse markRepaired(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @PathVariable String assetTag,
            @Valid @RequestBody AssetLifecycleRequest request
    ) {
        return AssetResponse.from(assetManagementApplicationService.markRepaired(command(tenantId, assetTag, request)));
    }

    @GetMapping("/assets/{assetTag}/history")
    public List<AssetLifecycleRecordResponse> history(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @PathVariable String assetTag
    ) {
        return assetManagementApplicationService.history(tenantId, assetTag).stream()
                .map(AssetLifecycleRecordResponse::from)
                .toList();
    }

    private static AssetLifecycleCommand command(String tenantId, String assetTag, AssetLifecycleRequest request) {
        return new AssetLifecycleCommand(tenantId, assetTag, request.actorId(), request.note());
    }
}
