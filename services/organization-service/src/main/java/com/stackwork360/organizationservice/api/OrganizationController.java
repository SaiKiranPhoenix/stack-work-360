package com.stackwork360.organizationservice.api;

import com.stackwork360.common.CorrelationIds;
import com.stackwork360.organizationservice.application.CreateOrgUnitCommand;
import com.stackwork360.organizationservice.application.OrganizationApplicationService;
import com.stackwork360.organizationservice.application.UpdateOrgUnitCommand;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/organization/v1/org-units")
public class OrganizationController {
    private final OrganizationApplicationService organizationApplicationService;

    public OrganizationController(OrganizationApplicationService organizationApplicationService) {
        this.organizationApplicationService = organizationApplicationService;
    }

    @PostMapping
    public ResponseEntity<OrgUnitResponse> create(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @Valid @RequestBody CreateOrgUnitRequest request
    ) {
        OrgUnitResponse response = OrgUnitResponse.from(organizationApplicationService.create(new CreateOrgUnitCommand(
                tenantId,
                request.name(),
                request.type(),
                request.parentId(),
                request.leaderWorkerId(),
                request.locationCode(),
                request.costCenterCode()
        )));

        return ResponseEntity.created(URI.create("/api/organization/v1/org-units/" + response.id()))
                .body(response);
    }

    @GetMapping
    public List<OrgUnitResponse> list(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return organizationApplicationService.list(tenantId).stream()
                .map(OrgUnitResponse::from)
                .toList();
    }

    @GetMapping("/{orgUnitId}")
    public OrgUnitResponse get(@PathVariable UUID orgUnitId) {
        return OrgUnitResponse.from(organizationApplicationService.get(orgUnitId));
    }

    @GetMapping("/org-chart")
    public List<OrgChartNodeResponse> orgChart(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return organizationApplicationService.orgChart(tenantId).stream()
                .map(OrgChartNodeResponse::from)
                .toList();
    }

    @PutMapping("/{orgUnitId}")
    public OrgUnitResponse update(
            @PathVariable UUID orgUnitId,
            @Valid @RequestBody UpdateOrgUnitRequest request
    ) {
        return OrgUnitResponse.from(organizationApplicationService.update(orgUnitId, new UpdateOrgUnitCommand(
                request.name(),
                request.parentId(),
                request.leaderWorkerId(),
                request.locationCode(),
                request.costCenterCode()
        )));
    }

    @PatchMapping("/{orgUnitId}/deactivate")
    public OrgUnitResponse deactivate(@PathVariable UUID orgUnitId) {
        return OrgUnitResponse.from(organizationApplicationService.deactivate(orgUnitId));
    }
}
