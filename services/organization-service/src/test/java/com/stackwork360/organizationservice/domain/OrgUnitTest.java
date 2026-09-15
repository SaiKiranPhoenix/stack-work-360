package com.stackwork360.organizationservice.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class OrgUnitTest {
    @Test
    void createsActiveOrgUnit() {
        OrgUnit orgUnit = OrgUnit.create(
                "tenant-1",
                "Engineering",
                OrgUnitType.DEPARTMENT,
                null,
                "worker-1",
                "blr",
                "eng"
        );

        assertEquals("Engineering", orgUnit.name());
        assertEquals(OrgUnitType.DEPARTMENT, orgUnit.type());
        assertEquals("BLR", orgUnit.locationCode());
        assertEquals("ENG", orgUnit.costCenterCode());
    }

    @Test
    void deactivatesOrgUnit() {
        OrgUnit orgUnit = OrgUnit.create(
                "tenant-1",
                "Platform",
                OrgUnitType.TEAM,
                null,
                null,
                null,
                null
        );

        orgUnit.deactivate();

        assertFalse(orgUnit.active());
    }

    @Test
    void rejectsBlankName() {
        assertThrows(IllegalArgumentException.class, () -> OrgUnit.create(
                "tenant-1",
                " ",
                OrgUnitType.TEAM,
                null,
                null,
                null,
                null
        ));
    }
}
