package com.stackwork360.tenantservice.api;

import com.stackwork360.tenantservice.application.AssignTenantFeaturesCommand;
import com.stackwork360.tenantservice.application.CreateTenantCommand;
import com.stackwork360.tenantservice.application.TenantApplicationService;
import com.stackwork360.tenantservice.application.UpdateTenantCommand;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tenants/v1/tenants")
public class TenantController {
    private final TenantApplicationService tenantApplicationService;

    public TenantController(TenantApplicationService tenantApplicationService) {
        this.tenantApplicationService = tenantApplicationService;
    }

    @PostMapping
    public ResponseEntity<TenantResponse> create(@Valid @RequestBody CreateTenantRequest request) {
        TenantResponse response = TenantResponse.from(tenantApplicationService.create(new CreateTenantCommand(
                request.slug(),
                request.displayName(),
                request.plan(),
                request.dataResidencyRegion(),
                request.retentionDays(),
                request.enabledFeatures(),
                request.configuration()
        )));

        return ResponseEntity.created(URI.create("/api/tenants/v1/tenants/" + response.id()))
                .body(response);
    }

    @GetMapping
    public List<TenantResponse> list() {
        return tenantApplicationService.list().stream()
                .map(TenantResponse::from)
                .toList();
    }

    @GetMapping("/{tenantId}")
    public TenantResponse get(@PathVariable UUID tenantId) {
        return TenantResponse.from(tenantApplicationService.get(tenantId));
    }

    @PutMapping("/{tenantId}")
    public TenantResponse update(
            @PathVariable UUID tenantId,
            @Valid @RequestBody UpdateTenantRequest request
    ) {
        return TenantResponse.from(tenantApplicationService.update(tenantId, new UpdateTenantCommand(
                request.displayName(),
                request.plan(),
                request.dataResidencyRegion(),
                request.retentionDays(),
                request.enabledFeatures(),
                request.configuration()
        )));
    }

    @PatchMapping("/{tenantId}/suspend")
    public TenantResponse suspend(@PathVariable UUID tenantId) {
        return TenantResponse.from(tenantApplicationService.suspend(tenantId));
    }

    @PatchMapping("/{tenantId}/reactivate")
    public TenantResponse reactivate(@PathVariable UUID tenantId) {
        return TenantResponse.from(tenantApplicationService.reactivate(tenantId));
    }

    @PutMapping("/{tenantId}/features")
    public TenantResponse assignFeatures(
            @PathVariable UUID tenantId,
            @Valid @RequestBody AssignTenantFeaturesRequest request
    ) {
        return TenantResponse.from(tenantApplicationService.assignFeatures(
                tenantId,
                new AssignTenantFeaturesCommand(request.enabledFeatures())
        ));
    }

    @GetMapping("/{tenantId}/entitlements")
    public TenantEntitlementsResponse entitlements(@PathVariable UUID tenantId) {
        return TenantEntitlementsResponse.from(tenantApplicationService.entitlements(tenantId));
    }
}
