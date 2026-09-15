package com.stackwork360.organizationservice.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.stackwork360.organizationservice.domain.OrgUnit;
import com.stackwork360.organizationservice.domain.OrgUnitType;
import com.stackwork360.organizationservice.infrastructure.InMemoryOrgUnitRepository;
import java.util.List;
import org.junit.jupiter.api.Test;

class OrganizationApplicationServiceTest {
    private final OrganizationApplicationService service =
            new OrganizationApplicationService(new InMemoryOrgUnitRepository());

    @Test
    void createsOrgUnit() {
        OrgUnit engineering = service.create(command("Engineering", OrgUnitType.DEPARTMENT, null));

        assertEquals("Engineering", engineering.name());
        assertEquals(OrgUnitType.DEPARTMENT, engineering.type());
    }

    @Test
    void buildsOrgChart() {
        OrgUnit engineering = service.create(command("Engineering", OrgUnitType.DEPARTMENT, null));
        service.create(command("Platform", OrgUnitType.TEAM, engineering.id()));
        service.create(command("Product", OrgUnitType.TEAM, engineering.id()));

        List<OrgChartNode> chart = service.orgChart("tenant-1");

        assertEquals(1, chart.size());
        assertEquals("Engineering", chart.get(0).name());
        assertEquals(2, chart.get(0).children().size());
    }

    @Test
    void rejectsParentFromAnotherTenant() {
        OrgUnit external = service.create(new CreateOrgUnitCommand(
                "tenant-2",
                "External",
                OrgUnitType.DEPARTMENT,
                null,
                null,
                null,
                null
        ));

        assertThrows(IllegalArgumentException.class, () -> service.create(command(
                "Platform",
                OrgUnitType.TEAM,
                external.id()
        )));
    }

    @Test
    void rejectsHierarchyCycle() {
        OrgUnit engineering = service.create(command("Engineering", OrgUnitType.DEPARTMENT, null));
        OrgUnit platform = service.create(command("Platform", OrgUnitType.TEAM, engineering.id()));

        assertThrows(IllegalArgumentException.class, () -> service.update(engineering.id(), new UpdateOrgUnitCommand(
                "Engineering",
                platform.id(),
                null,
                null,
                null
        )));
    }

    @Test
    void preventsDeactivatingParentWithChildren() {
        OrgUnit engineering = service.create(command("Engineering", OrgUnitType.DEPARTMENT, null));
        service.create(command("Platform", OrgUnitType.TEAM, engineering.id()));

        assertThrows(IllegalStateException.class, () -> service.deactivate(engineering.id()));
    }

    private static CreateOrgUnitCommand command(String name, OrgUnitType type, java.util.UUID parentId) {
        return new CreateOrgUnitCommand(
                "tenant-1",
                name,
                type,
                parentId,
                null,
                "BLR",
                "ENG"
        );
    }
}
