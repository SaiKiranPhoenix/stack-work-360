package com.stackwork360.organizationservice.application;

import com.stackwork360.organizationservice.domain.OrgUnit;
import com.stackwork360.organizationservice.domain.OrgUnitType;
import java.util.List;
import java.util.UUID;

public record OrgChartNode(
        UUID id,
        String name,
        OrgUnitType type,
        String leaderWorkerId,
        String locationCode,
        String costCenterCode,
        List<OrgChartNode> children
) {
    public static OrgChartNode from(OrgUnit orgUnit, List<OrgChartNode> children) {
        return new OrgChartNode(
                orgUnit.id(),
                orgUnit.name(),
                orgUnit.type(),
                orgUnit.leaderWorkerId(),
                orgUnit.locationCode(),
                orgUnit.costCenterCode(),
                children
        );
    }
}
