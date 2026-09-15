package com.stackwork360.organizationservice.application;

import com.stackwork360.organizationservice.domain.OrgUnit;
import com.stackwork360.organizationservice.domain.OrgUnitRepository;
import com.stackwork360.web.ResourceNotFoundException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class OrganizationApplicationService {
    private final OrgUnitRepository orgUnitRepository;

    public OrganizationApplicationService(OrgUnitRepository orgUnitRepository) {
        this.orgUnitRepository = orgUnitRepository;
    }

    public OrgUnit create(CreateOrgUnitCommand command) {
        if (orgUnitRepository.existsByTenantIdAndName(command.tenantId(), command.name())) {
            throw new IllegalArgumentException("org unit name is already in use for tenant");
        }
        validateParent(command.tenantId(), command.parentId(), null);

        OrgUnit orgUnit = OrgUnit.create(
                command.tenantId(),
                command.name(),
                command.type(),
                command.parentId(),
                command.leaderWorkerId(),
                command.locationCode(),
                command.costCenterCode()
        );
        return orgUnitRepository.save(orgUnit);
    }

    public OrgUnit update(UUID orgUnitId, UpdateOrgUnitCommand command) {
        OrgUnit orgUnit = get(orgUnitId);
        validateParent(orgUnit.tenantId(), command.parentId(), orgUnitId);
        orgUnit.update(
                command.name(),
                command.parentId(),
                command.leaderWorkerId(),
                command.locationCode(),
                command.costCenterCode()
        );
        return orgUnitRepository.save(orgUnit);
    }

    public OrgUnit deactivate(UUID orgUnitId) {
        OrgUnit orgUnit = get(orgUnitId);
        if (!orgUnitRepository.findChildren(orgUnit.tenantId(), orgUnitId).isEmpty()) {
            throw new IllegalStateException("org unit with children cannot be deactivated");
        }
        orgUnit.deactivate();
        return orgUnitRepository.save(orgUnit);
    }

    public OrgUnit get(UUID orgUnitId) {
        return orgUnitRepository.findById(orgUnitId)
                .orElseThrow(() -> new ResourceNotFoundException("org unit not found"));
    }

    public List<OrgUnit> list(String tenantId) {
        return orgUnitRepository.findByTenantId(tenantId);
    }

    public List<OrgChartNode> orgChart(String tenantId) {
        return orgUnitRepository.findByTenantId(tenantId).stream()
                .filter(OrgUnit::active)
                .filter(orgUnit -> orgUnit.parentId() == null)
                .sorted(Comparator.comparing(OrgUnit::name))
                .map(orgUnit -> buildNode(tenantId, orgUnit))
                .toList();
    }

    private OrgChartNode buildNode(String tenantId, OrgUnit orgUnit) {
        List<OrgChartNode> children = orgUnitRepository.findChildren(tenantId, orgUnit.id()).stream()
                .filter(OrgUnit::active)
                .sorted(Comparator.comparing(OrgUnit::name))
                .map(child -> buildNode(tenantId, child))
                .toList();
        return OrgChartNode.from(orgUnit, children);
    }

    private void validateParent(String tenantId, UUID parentId, UUID currentOrgUnitId) {
        if (parentId == null) {
            return;
        }
        OrgUnit parent = orgUnitRepository.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("parent org unit does not exist"));
        if (!parent.tenantId().equals(tenantId)) {
            throw new IllegalArgumentException("parent org unit belongs to another tenant");
        }
        if (parentId.equals(currentOrgUnitId)) {
            throw new IllegalArgumentException("org unit cannot be its own parent");
        }
        if (currentOrgUnitId != null && isDescendant(tenantId, parentId, currentOrgUnitId, new HashSet<>())) {
            throw new IllegalArgumentException("org unit hierarchy cannot contain cycles");
        }
    }

    private boolean isDescendant(String tenantId, UUID candidateId, UUID ancestorId, HashSet<UUID> visited) {
        if (!visited.add(candidateId)) {
            return false;
        }
        if (candidateId.equals(ancestorId)) {
            return true;
        }
        return orgUnitRepository.findChildren(tenantId, candidateId).stream()
                .anyMatch(child -> isDescendant(tenantId, child.id(), ancestorId, visited));
    }
}
