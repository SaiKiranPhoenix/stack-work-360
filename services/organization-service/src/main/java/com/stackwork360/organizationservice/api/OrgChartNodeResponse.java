package com.stackwork360.organizationservice.api;

import com.stackwork360.organizationservice.application.OrgChartNode;
import com.stackwork360.organizationservice.domain.OrgUnitType;
import java.util.List;
import java.util.UUID;

public record OrgChartNodeResponse(
        UUID id,
        String name,
        OrgUnitType type,
        String leaderWorkerId,
        String locationCode,
        String costCenterCode,
        List<OrgChartNodeResponse> children
) {
    public static OrgChartNodeResponse from(OrgChartNode node) {
        return new OrgChartNodeResponse(
                node.id(),
                node.name(),
                node.type(),
                node.leaderWorkerId(),
                node.locationCode(),
                node.costCenterCode(),
                node.children().stream().map(OrgChartNodeResponse::from).toList()
        );
    }
}
