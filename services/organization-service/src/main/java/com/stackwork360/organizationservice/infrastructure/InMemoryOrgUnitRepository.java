package com.stackwork360.organizationservice.infrastructure;

import com.stackwork360.organizationservice.domain.OrgUnit;
import com.stackwork360.organizationservice.domain.OrgUnitRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryOrgUnitRepository implements OrgUnitRepository {
    private final ConcurrentMap<UUID, OrgUnit> orgUnitsById = new ConcurrentHashMap<>();

    @Override
    public OrgUnit save(OrgUnit orgUnit) {
        orgUnitsById.put(orgUnit.id(), orgUnit);
        return orgUnit;
    }

    @Override
    public Optional<OrgUnit> findById(UUID id) {
        return Optional.ofNullable(orgUnitsById.get(id));
    }

    @Override
    public List<OrgUnit> findByTenantId(String tenantId) {
        return orgUnitsById.values().stream()
                .filter(orgUnit -> orgUnit.tenantId().equals(tenantId))
                .sorted(Comparator.comparing(OrgUnit::createdAt))
                .toList();
    }

    @Override
    public List<OrgUnit> findChildren(String tenantId, UUID parentId) {
        return orgUnitsById.values().stream()
                .filter(orgUnit -> orgUnit.tenantId().equals(tenantId))
                .filter(orgUnit -> parentId.equals(orgUnit.parentId()))
                .sorted(Comparator.comparing(OrgUnit::name))
                .toList();
    }

    @Override
    public boolean existsByTenantIdAndName(String tenantId, String name) {
        return orgUnitsById.values().stream()
                .filter(orgUnit -> orgUnit.tenantId().equals(tenantId))
                .anyMatch(orgUnit -> orgUnit.name().equalsIgnoreCase(name));
    }
}
